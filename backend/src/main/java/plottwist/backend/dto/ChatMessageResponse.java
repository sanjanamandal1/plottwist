package plottwist.backend.dto;

import java.util.List;

public record ChatMessageResponse(
        String id,
        String role,
        String content,
        List<CitationDto> citations,
        String createdAt
) {}
