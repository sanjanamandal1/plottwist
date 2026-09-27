package plottwist.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import plottwist.backend.entity.Repository;

public interface RepositoryRepository extends JpaRepository<Repository, UUID> {

    List<Repository> findAllByUserId(UUID userId);

    Optional<Repository> findByIdAndUserId(UUID id, UUID userId);

    Optional<Repository> findByGithubIdAndUserId(Long githubId, UUID userId);
}
