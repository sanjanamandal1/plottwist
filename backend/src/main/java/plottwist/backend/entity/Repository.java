package plottwist.backend.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A GitHub repository imported by a user.
 * <p>
 * Tracks metadata synced from the GitHub API as well as the current state
 * of the RAG indexing pipeline (status, progress counters, error messages).
 * </p>
 */
@Entity
@Table(
    name = "repositories",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "github_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** GitHub numeric repository ID. */
    @Column(name = "github_id", nullable = false)
    private Long githubId;

    /** Repository owner (user or org login). */
    @Column(nullable = false)
    private String owner;

    /** Repository short name. */
    @Column(nullable = false)
    private String name;

    /** owner/name — convenience for display. */
    @Column(nullable = false)
    private String fullName;

    @Column(length = 1024)
    private String description;

    /** Primary programming language (e.g. "Java", "TypeScript"). */
    private String language;

    private String defaultBranch;

    @Column(name = "is_private", nullable = false)
    private boolean privateRepo;

    private String htmlUrl;

    private Integer stargazersCount;

    // ── Indexing state ──────────────────────────────────────────────────

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IndexStatus indexStatus = IndexStatus.NONE;

    @Builder.Default
    private int filesTotal = 0;

    @Builder.Default
    private int filesProcessed = 0;

    @Builder.Default
    private int chunkCount = 0;

    @Column(length = 2048)
    private String errorMessage;

    private Instant indexedAt;

    // ── Timestamps ──────────────────────────────────────────────────────

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Builder.Default
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();
}
