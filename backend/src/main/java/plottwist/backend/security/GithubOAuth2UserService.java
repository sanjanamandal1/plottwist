package plottwist.backend.security;

import java.time.Instant;
import java.util.Map;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import plottwist.backend.entity.User;
import plottwist.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Custom OAuth2UserService that upserts the GitHub user into our database
 * and encrypts the access token before storage.
 */
@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final TextEncryptor textEncryptor;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attrs = oAuth2User.getAttributes();

        Long githubId = ((Number) attrs.get("id")).longValue();
        String login = (String) attrs.get("login");
        String name = (String) attrs.get("name");
        String avatar = (String) attrs.get("avatar_url");
        String token = request.getAccessToken().getTokenValue();

        User user = userRepository.findByGithubId(githubId)
                .map(existing -> {
                    existing.setGithubUsername(login);
                    existing.setDisplayName(name != null ? name : login);
                    existing.setAvatarUrl(avatar);
                    existing.setEncryptedAccessToken(textEncryptor.encrypt(token));
                    existing.setUpdatedAt(Instant.now());
                    return userRepository.save(existing);
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .githubId(githubId)
                        .githubUsername(login)
                        .displayName(name != null ? name : login)
                        .avatarUrl(avatar)
                        .encryptedAccessToken(textEncryptor.encrypt(token))
                        .build()));

        return new AppUserPrincipal(user, attrs, oAuth2User.getAuthorities());
    }
}
