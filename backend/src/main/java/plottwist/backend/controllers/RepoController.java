package plottwist.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import plottwist.backend.dto.IndexStatusResponse;
import plottwist.backend.dto.RepositoryResponse;
import plottwist.backend.entity.Repository;
import plottwist.backend.security.CurrentUser;
import plottwist.backend.services.RepoService;
import plottwist.backend.services.indexing.IndexingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
public class RepoController {

    private final CurrentUser currentUser;
    private final RepoService repoService;
    private final IndexingService indexingService;

    @GetMapping
    public List<RepositoryResponse> list(@RequestParam(defaultValue = "true") boolean refresh) {
        return repoService.listRepos(currentUser.requireId(), refresh);
    }

    @GetMapping("/{id}")
    public RepositoryResponse get(@PathVariable UUID id) {
        return repoService.getRepo(id, currentUser.requireId());
    }

    @GetMapping("/{id}/status")
    public IndexStatusResponse status(@PathVariable UUID id) {
        return repoService.getIndexStatus(id, currentUser.requireId());
    }

    /** Kicks off the async indexing pipeline and returns immediately. */
    @PostMapping("/{id}/index")
    public RepositoryResponse startIndex(@PathVariable UUID id) {
        UUID userId = currentUser.requireId();
        Repository repo = indexingService.startIndexing(id, userId);
        indexingService.indexAsync(id, userId);
        return repoService.toResponse(repo);
    }
}
