package plottwist.backend.security;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import plottwist.backend.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Wraps our internal {@link User} while implementing {@link OAuth2User}
 * so Spring Security can carry the principal through the filter chain.
 */
@RequiredArgsConstructor
@Getter
public class AppUserPrincipal implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public UUID getUserId() {
        return user.getId();
    }

    @Override
    public String getName() {
        return user.getGithubId().toString();
    }
}
