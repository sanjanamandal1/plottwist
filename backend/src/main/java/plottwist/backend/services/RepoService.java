package plottwist.backend.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import plottwist.backend.dto.IndexStatusResponse;
import plottwist.backend.dto.RepositoryResponse;
import plottwist.backend.entity.IndexStatus;
import plottwist.backend.entity.Repository;
import plottwist.backend.entity.User;
import plottwist.backend.exceptions.NotFoundException;
import plottwist.backend.repository.RepositoryRepository;
import plottwist.backend.services.github.GithubApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Manages GitHub repository sync and provides DTO conversions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RepoService {

    private final RepositoryRepository repositoryRepository;
    private final UserService userService;
    private final GithubApiClient githubApiClient;

    /**
     * Lists the user's repositories. When {@code refresh} is true,
     * fetches the latest list from GitHub and upserts into the database.
     */
    @SuppressWarnings("unchecked")
    public List<RepositoryResponse> listRepos(UUID userId, boolean refresh) {
        User user = userService.requiredById(userId);

        if (refresh) {
            String token = userService.decryptAccessToken(user);
            List<Map<String, Object>> ghRepos = githubApiClient.listUserRepos(token);

            for (Map<String, Object> gh : ghRepos) {
                Long ghId = ((Number) gh.get("id")).longValue();
                Map<String, Object> ownerMap = (Map<String, Object>) gh.get("owner");

                repositoryRepository.findByGithubIdAndUserId(ghId, userId)
                        .map(existing -> {
                            updateFromGitHub(existing, gh, ownerMap);
                            return repositoryRepository.save(existing);
                        })
                        .orElseGet(() -> repositoryRepository.save(buildFromGitHub(user, gh, ownerMap, ghId)));
            }
        }

        return repositoryRepository.findAllByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public RepositoryResponse getRepo(UUID repoId, UUID userId) {
        Repository repo = repositoryRepository.findByIdAndUserId(repoId, userId)
                .orElseThrow(() -> new NotFoundException("Repository not found"));
        return toResponse(repo);
    }

    public IndexStatusResponse getIndexStatus(UUID repoId, UUID userId) {
        Repository repo = repositoryRepository.findByIdAndUserId(repoId, userId)
                .orElseThrow(() -> new NotFoundException("Repository not found"));
        return new IndexStatusResponse(
                repo.getIndexStatus().name(),
                repo.getFilesTotal(),
                repo.getFilesProcessed(),
                repo.getChunkCount(),
                repo.getErrorMessage(),
                repo.getIndexedAt() != null ? repo.getIndexedAt().toString() : null
        );
    }

    // ── Private helpers ─────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void updateFromGitHub(Repository repo, Map<String, Object> gh, Map<String, Object> ownerMap) {
        repo.setOwner(str(ownerMap.get("login")));
        repo.setName(str(gh.get("name")));
        repo.setFullName(str(gh.get("full_name")));
        repo.setDescription(str(gh.get("description")));
        repo.setLanguage(str(gh.get("language")));
        repo.setDefaultBranch(str(gh.get("default_branch")));
        repo.setPrivateRepo(Boolean.TRUE.equals(gh.get("private")));
        repo.setHtmlUrl(str(gh.get("html_url")));
        repo.setStargazersCount(gh.get("stargazers_count") instanceof Number n ? n.intValue() : 0);
        repo.setUpdatedAt(Instant.now());
    }

    @SuppressWarnings("unchecked")
    private Repository buildFromGitHub(User user, Map<String, Object> gh, Map<String, Object> ownerMap, Long ghId) {
        return Repository.builder()
                .user(user)
                .githubId(ghId)
                .owner(str(ownerMap.get("login")))
                .name(str(gh.get("name")))
                .fullName(str(gh.get("full_name")))
                .description(str(gh.get("description")))
                .language(str(gh.get("language")))
                .defaultBranch(str(gh.get("default_branch")))
                .privateRepo(Boolean.TRUE.equals(gh.get("private")))
                .htmlUrl(str(gh.get("html_url")))
                .stargazersCount(gh.get("stargazers_count") instanceof Number n ? n.intValue() : 0)
                .indexStatus(IndexStatus.NONE)
                .build();
    }

    public RepositoryResponse toResponse(Repository repo) {
        return new RepositoryResponse(
                repo.getId().toString(),
                repo.getOwner(),
                repo.getName(),
                repo.getFullName(),
                repo.getDescription(),
                repo.getLanguage(),
                repo.getDefaultBranch(),
                repo.isPrivateRepo(),
                repo.getHtmlUrl(),
                repo.getStargazersCount(),
                repo.getIndexStatus().name(),
                repo.getFilesTotal(),
                repo.getFilesProcessed(),
                repo.getChunkCount(),
                repo.getErrorMessage(),
                repo.getIndexedAt() != null ? repo.getIndexedAt().toString() : null,
                repo.getCreatedAt().toString(),
                repo.getUpdatedAt().toString()
        );
    }

    private String str(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}
