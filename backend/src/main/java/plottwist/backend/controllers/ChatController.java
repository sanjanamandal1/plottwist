package plottwist.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import plottwist.backend.dto.ChatMessageRequest;
import plottwist.backend.dto.ChatMessageResponse;
import plottwist.backend.dto.ChatSessionResponse;
import plottwist.backend.dto.CreateChatSessionRequest;
import plottwist.backend.entity.ChatSession;
import plottwist.backend.security.CurrentUser;
import plottwist.backend.services.ChatService;
import plottwist.backend.services.ai.ChatStreamHandler;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final CurrentUser currentUser;
    private final ChatService chatService;
    private final ChatStreamHandler chatStreamHandler;

    @PostMapping("/sessions")
    public ChatSessionResponse createSession(@RequestBody CreateChatSessionRequest req) {
        return chatService.createSession(req, currentUser.requireId());
    }

    @GetMapping("/sessions")
    public List<ChatSessionResponse> listSessions(@RequestParam String repositoryId) {
        return chatService.listSessions(UUID.fromString(repositoryId), currentUser.requireId());
    }

    @GetMapping("/sessions/{sessionId}")
    public List<ChatMessageResponse> getMessages(@PathVariable UUID sessionId) {
        return chatService.getMessages(sessionId, currentUser.requireId());
    }

    /**
     * Sends a message and streams the AI response as Server-Sent Events.
     * The SSE stream emits: user_message, token (many), assistant_message, done.
     */
    @PostMapping(value = "/sessions/{sessionId}/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendMessage(
            @PathVariable UUID sessionId,
            @RequestBody ChatMessageRequest req
    ) {
        UUID userId = currentUser.requireId();
        ChatSession session = chatService.requireSession(sessionId, userId);

        SseEmitter emitter = new SseEmitter(120_000L); // 2 min timeout
        chatStreamHandler.handleMessage(emitter, session, req.content());
        return emitter;
    }
}
