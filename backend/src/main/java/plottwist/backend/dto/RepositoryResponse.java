package plottwist.backend.dto;

public record RepositoryResponse(
        String id,
        String owner,
        String name,
        String fullName,
        String description,
        String language,
        String defaultBranch,
        boolean privateRepo,
        String htmlUrl,
        Integer stargazersCount,
        String indexStatus,
        int filesTotal,
        int filesProcessed,
        int chunkCount,
        String errorMessage,
        String indexedAt,
        String createdAt,
        String updatedAt
) {}
