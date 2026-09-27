package plottwist.backend.services.github;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple rate limiter to avoid hitting GitHub's API rate limits
 * during repository tree traversal and file fetching.
 */
@Component
@Slf4j
public class GitHubRateLimiter {

    private static final long PAUSE_MS = 100;

    /** Pauses the current thread briefly to respect API limits. */
    public void pause() {
        try {
            Thread.sleep(PAUSE_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
