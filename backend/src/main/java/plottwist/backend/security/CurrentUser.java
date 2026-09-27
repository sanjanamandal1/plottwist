package plottwist.backend.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import plottwist.backend.entity.User;
import plottwist.backend.exceptions.UnauthorizedException;

/**
 * Convenience component to extract the authenticated PlotTwist user
 * from the current security context.
 */
@Component
public class CurrentUser {

    /** Returns the internal {@link User} or throws 401. */
    public User require() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AppUserPrincipal principal) {
            return principal.getUser();
        }
        throw new UnauthorizedException("Not authenticated");
    }

    /** Returns the user's internal UUID or throws 401. */
    public UUID requireId() {
        return require().getId();
    }
}
