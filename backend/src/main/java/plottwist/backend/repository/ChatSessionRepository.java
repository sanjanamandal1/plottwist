package plottwist.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import plottwist.backend.entity.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {

    List<ChatSession> findAllByRepositoryIdAndUserIdOrderByCreatedAtDesc(
            UUID repositoryId, UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
