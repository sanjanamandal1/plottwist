"use client";

import Link from "next/link";
import { Database, ExternalLink, Lock, MessageSquareCode, Star } from "lucide-react";

import { type Repository } from "@/lib/api";
import { useStartIndexing, getRepoProgress } from "@/hooks/use-repos";
import { Button, buttonVariants } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";

function StatusBadge({ status }: { status: Repository["indexStatus"] }) {
  const map = {
    NONE:     { label: "Not indexed", variant: "secondary" as const },
    INDEXING: { label: "Indexing…",   variant: "default" as const },
    READY:    { label: "Ready",       variant: "default" as const },
    FAILED:   { label: "Failed",      variant: "destructive" as const },
  };
  const { label, variant } = map[status];
  return <Badge variant={variant}>{label}</Badge>;
}

export function RepoCard({ repo }: { repo: Repository }) {
  const startIndex = useStartIndexing();
  const progress = getRepoProgress(repo);

  return (
    <Card className="flex flex-col transition-shadow hover:shadow-md">
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2">
          <CardTitle className="text-sm font-medium leading-tight">
            <span className="text-muted-foreground">{repo.owner}/</span>
            {repo.name}
          </CardTitle>
          <StatusBadge status={repo.indexStatus} />
        </div>
        {repo.description && (
          <p className="text-xs text-muted-foreground line-clamp-2 mt-1">
            {repo.description}
          </p>
        )}
      </CardHeader>

      <CardContent className="flex-1 space-y-3 pb-3">
        <div className="flex items-center gap-3 text-xs text-muted-foreground">
          {repo.language && (
            <span className="flex items-center gap-1">
              <span className="size-2.5 rounded-full bg-primary" />
              {repo.language}
            </span>
          )}
          {repo.stargazersCount != null && repo.stargazersCount > 0 && (
            <span className="flex items-center gap-1">
              <Star className="size-3" />
              {repo.stargazersCount}
            </span>
          )}
          {repo.privateRepo && (
            <span className="flex items-center gap-1">
              <Lock className="size-3" />
              Private
            </span>
          )}
        </div>

        {repo.indexStatus === "INDEXING" && (
          <div className="space-y-1">
            <Progress value={progress} className="h-1.5" />
            <p className="text-[11px] text-muted-foreground">
              {repo.filesProcessed}/{repo.filesTotal} files • {repo.chunkCount} chunks
            </p>
          </div>
        )}

        {repo.indexStatus === "READY" && (
          <p className="text-[11px] text-muted-foreground">
            <Database className="mr-1 inline size-3" />
            {repo.chunkCount} chunks from {repo.filesProcessed} files
          </p>
        )}

        {repo.indexStatus === "FAILED" && repo.errorMessage && (
          <p className="text-[11px] text-destructive line-clamp-2">
            {repo.errorMessage}
          </p>
        )}
      </CardContent>

      <CardFooter className="gap-2 pt-0">
        {repo.indexStatus === "NONE" || repo.indexStatus === "FAILED" ? (
          <Button
            size="sm"
            variant="outline"
            className="flex-1"
            onClick={() => startIndex.mutate(repo.id)}
            disabled={startIndex.isPending}
          >
            <Database className="size-3.5" />
            Index
          </Button>
        ) : null}

        {repo.indexStatus === "READY" && (
          <Link
            href={`/chat/${repo.id}`}
            className={cn(buttonVariants({ size: "sm" }), "flex-1")}
          >
            <MessageSquareCode className="size-3.5" />
            Chat
          </Link>
        )}

        <a
          href={repo.htmlUrl}
          target="_blank"
          rel="noopener noreferrer"
          className={cn(buttonVariants({ size: "sm", variant: "ghost" }))}
        >
          <ExternalLink className="size-3.5" />
        </a>
      </CardFooter>
    </Card>
  );
}
