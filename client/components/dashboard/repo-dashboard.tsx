"use client";

import { RefreshCw } from "lucide-react";
import { useRepos, useRefreshRepos } from "@/hooks/use-repos";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { RepoCard } from "@/components/dashboard/repo-card";

export function RepoDashboard() {
  const { data: repos, isLoading } = useRepos();
  const refresh = useRefreshRepos();

  if (isLoading) {
    return (
      <div className="flex flex-1 items-center justify-center p-8">
        <Spinner className="size-6" />
      </div>
    );
  }

  return (
    <div className="space-y-6 p-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="font-heading text-lg font-semibold">
            {repos?.length ?? 0} repositories
          </h2>
          <p className="text-sm text-muted-foreground">
            Index a repo, then chat with its code.
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          onClick={() => refresh.mutate()}
          disabled={refresh.isPending}
        >
          <RefreshCw className={`size-4 ${refresh.isPending ? "animate-spin" : ""}`} />
          Sync
        </Button>
      </div>

      {repos && repos.length > 0 ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {repos.map((repo) => (
            <RepoCard key={repo.id} repo={repo} />
          ))}
        </div>
      ) : (
        <div className="rounded-lg border border-dashed p-12 text-center">
          <p className="text-muted-foreground">
            No repositories found. Click <strong>Sync</strong> to pull your repos from GitHub.
          </p>
        </div>
      )}
    </div>
  );
}
