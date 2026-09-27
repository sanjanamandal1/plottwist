package plottwist.backend.services.ai;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;

import plottwist.backend.dto.CitationDto;
import plottwist.backend.entity.ChatMessage;
import plottwist.backend.entity.ChatSession;
import plottwist.backend.entity.MessageRole;
import plottwist.backend.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Orchestrates the full RAG-powered chat flow:
 * <ol>
 *   <li>Save the user's message</li>
 *   <li>Retrieve relevant code context from the vector store</li>
 *   <li>Build the prompt and stream the AI response via SSE</li>
 *   <li>Extract citations and persist the assistant's message</li>
 * </ol>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatStreamHandler {

    private final ChatService chatService;
    private final CodeContextRetriever contextRetriever;
    private final ChatPromptBuilder promptBuilder;
    private final CitationMapper citationMapper;
    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    /**
     * Handles a user message by streaming the AI response back via SSE.
     */
    public void handleMessage(
            SseEmitter emitter,
            ChatSession session,
            String userContent
    ) {
        try {
            // 1. Save user message
            ChatMessage userMsg = chatService.saveMessage(
                    session.getId(), MessageRole.USER, userContent, null);
            emitEvent(emitter, "user_message", chatService.toMessageResponse(userMsg));

            // 2. Retrieve context
            String repoId = session.getRepository().getId().toString();
            List<RetrievedContext> contexts = contextRetriever.retrieve(repoId, userContent);

            // 3. Build prompt and stream AI response
            String prompt = promptBuilder.build(userContent, contexts);
            ChatClient chatClient = chatClientBuilder.build();

            StringBuilder fullResponse = new StringBuilder();

            Flux<String> stream = chatClient.prompt()
                    .user(prompt)
                    .stream()
                    .content();

            stream.doOnNext(token -> {
                fullResponse.append(token);
                emitEvent(emitter, "token", token);
            })
            .doOnComplete(() -> {
                try {
                    // 4. Extract citations
                    String responseText = fullResponse.toString();
                    List<CitationDto> citations = citationMapper.extractCitations(responseText, contexts);
                    String citationsJson = objectMapper.writeValueAsString(citations);

                    // 5. Save assistant message
                    ChatMessage assistantMsg = chatService.saveMessage(
                            session.getId(), MessageRole.ASSISTANT, responseText, citationsJson);
                    emitEvent(emitter, "assistant_message", chatService.toMessageResponse(assistantMsg));
                    emitEvent(emitter, "done", "");
                    emitter.complete();
                } catch (Exception e) {
                    log.error("Failed to finalize assistant message", e);
                    emitter.completeWithError(e);
                }
            })
            .doOnError(err -> {
                log.error("AI stream error", err);
                emitter.completeWithError(err);
            })
            .subscribe();

        } catch (Exception e) {
            log.error("Chat handler error", e);
            emitter.completeWithError(e);
        }
    }

    private void emitEvent(SseEmitter emitter, String eventName, Object data) {
        try {
            String json = data instanceof String s ? "\"" + s.replace("\"", "\\\"").replace("\n", "\\n") + "\""
                    : objectMapper.writeValueAsString(data);
            emitter.send(SseEmitter.event().name(eventName).data(json));
        } catch (Exception e) {
            log.warn("SSE emit failed for event {}: {}", eventName, e.getMessage());
        }
    }
}
