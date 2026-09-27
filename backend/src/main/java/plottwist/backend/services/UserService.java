package plottwist.backend.services;

import java.util.UUID;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import plottwist.backend.dto.UserResponse;
import plottwist.backend.entity.User;
import plottwist.backend.exceptions.NotFoundException;
import plottwist.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * User lifecycle operations — lookup, DTO mapping, and token decryption.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TextEncryptor textEncryptor;

    public User requiredById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    /** Decrypts the stored OAuth access token for GitHub API calls. */
    public String decryptAccessToken(User user) {
        return textEncryptor.decrypt(user.getEncryptedAccessToken());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getGithubUsername(),
                user.getDisplayName(),
                user.getAvatarUrl()
        );
    }
}
