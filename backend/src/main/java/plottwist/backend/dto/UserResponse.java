package plottwist.backend.dto;

public record UserResponse(
        String id,
        String githubUsername,
        String displayName,
        String avatarUrl
) {}
