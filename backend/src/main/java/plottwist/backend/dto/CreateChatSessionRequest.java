package plottwist.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateChatSessionRequest(
        @NotBlank String repositoryId,
        String title
) {}
