package plottwist.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import plottwist.backend.dto.ChatMessageResponse;
import plottwist.backend.dto.ChatSessionResponse;
import plottwist.backend.dto.CitationDto;
import plottwist.backend.dto.CreateChatSessionRequest;
import plottwist.backend.entity.ChatMessage;
import plottwist.backend.entity.ChatSession;
import plottwist.backend.entity.MessageRole;
import plottwist.backend.entity.Repository;
import plottwist.backend.exceptions.NotFoundException;
import plottwist.backend.repository.ChatMessageRepository;
import plottwist.backend.repository.ChatSessionRepository;
import plottwist.backend.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;

/**
 * Manages chat sessions, message persistence, and DTO conversions.
 * <p>
 * The actual AI-powered response generation and streaming lives in
 * {@link plottwist.backend.services.ai.ChatStreamHandler}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final RepositoryRepository repositoryRepository;
    private final ObjectMapper objectMapper;

    public ChatSessionResponse createSession(CreateChatSessionRequest req, UUID userId) {
        UUID repoId = UUID.fromString(req.repositoryId());
        Repository repo = repositoryRepository.findByIdAndUserId(repoId, userId)
                .orElseThrow(() -> new NotFoundException("Repository not found"));

        ChatSession session = ChatSession.builder()
                .user(repo.getUser())
                .repository(repo)
                .title(req.title() != null ? req.title() : "New conversation")
                .build();
        session = sessionRepository.save(session);
        return toSessionResponse(session);
    }

    public List<ChatSessionResponse> listSessions(UUID repositoryId, UUID userId) {
        return sessionRepository.findAllByRepositoryIdAndUserIdOrderByCreatedAtDesc(repositoryId, userId)
                .stream()
                .map(this::toSessionResponse)
                .toList();
    }

    public List<ChatMessageResponse> getMessages(UUID sessionId, UUID userId) {
        if (!sessionRepository.existsByIdAndUserId(sessionId, userId)) {
            throw new NotFoundException("Chat session not found");
        }
        return messageRepository.findAllBySessionIdOrderByCreatedAtAsc(sessionId)
                .stream()
                .map(this::toMessageResponse)
                .toList();
    }

    /** Persists a user or assistant message to the database. */
    public ChatMessage saveMessage(UUID sessionId, MessageRole role, String content, String citationsJson) {
        ChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        ChatMessage msg = ChatMessage.builder()
                .session(session)
                .role(role)
                .content(content)
                .citationsJson(citationsJson)
                .build();
        return messageRepository.save(msg);
    }

    public ChatSession requireSession(UUID sessionId, UUID userId) {
        ChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Chat session not found"));
        if (!session.getUser().getId().equals(userId)) {
            throw new NotFoundException("Chat session not found");
        }
        return session;
    }

    // ── DTO mappers ─────────────────────────────────────────────────────

    private ChatSessionResponse toSessionResponse(ChatSession s) {
        return new ChatSessionResponse(
                s.getId().toString(),
                s.getTitle(),
                s.getCreatedAt().toString()
        );
    }

    public ChatMessageResponse toMessageResponse(ChatMessage m) {
        List<CitationDto> citations = List.of();
        if (m.getCitationsJson() != null) {
            try {
                citations = objectMapper.readValue(
                        m.getCitationsJson(), new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                // swallow parse errors — return empty citations
            }
        }
        return new ChatMessageResponse(
                m.getId().toString(),
                m.getRole().name(),
                m.getContent(),
                citations,
                m.getCreatedAt().toString()
        );
    }
}
