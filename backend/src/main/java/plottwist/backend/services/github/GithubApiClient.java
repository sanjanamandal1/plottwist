package plottwist.backend.services.github;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

/**
 * Thin wrapper around GitHub's REST API v3.
 * <p>
 * Provides methods for listing repos, fetching file trees, and
 * downloading individual file content — all authenticated via
 * the user's OAuth access token.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class GithubApiClient {

    private static final String API_BASE = "https://api.github.com";
    private final RestTemplate restTemplate;

    /** Lists all repositories visible to the authenticated user. */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> listUserRepos(String token) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders(token));
        var response = restTemplate.exchange(
                API_BASE + "/user/repos?per_page=100&sort=updated",
                HttpMethod.GET, entity, List.class);
        return response.getBody();
    }

    /** Fetches the recursive file tree for a repository. */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRepoTree(String token, String owner, String repo, String branch) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders(token));
        var response = restTemplate.exchange(
                API_BASE + "/repos/{owner}/{repo}/git/trees/{branch}?recursive=1",
                HttpMethod.GET, entity, Map.class,
                owner, repo, branch);
        return response.getBody();
    }

    /** Downloads and decodes a single file's content (base64). */
    @SuppressWarnings("unchecked")
    public String getFileContent(String token, String owner, String repo, String path) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders(token));
        var response = restTemplate.exchange(
                API_BASE + "/repos/{owner}/{repo}/contents/{path}",
                HttpMethod.GET, entity, Map.class,
                owner, repo, path);

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("content") == null) {
            return "";
        }

        String encoded = body.get("content").toString().replaceAll("\\s", "");
        return new String(Base64.getDecoder().decode(encoded));
    }

    private HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }
}
