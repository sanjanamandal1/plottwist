package plottwist.backend.dto;

public record IndexStatusResponse(
        String indexStatus,
        int filesTotal,
        int filesProcessed,
        int chunkCount,
        String errorMessage,
        String indexedAt
) {}
