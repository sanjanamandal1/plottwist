"use client";

import Link from "next/link";
import { ArrowLeft, Database, AlertTriangle } from "lucide-react";

import { type Repository } from "@/lib/api";
import { useStartIndexing, getRepoProgress } from "@/hooks/use-repos";
import { Button, buttonVariants } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { cn } from "@/lib/utils";

export function IndexingState({ repo }: { repo: Repository }) {
  const startIndex = useStartIndexing();
  const progress = getRepoProgress(repo);

  return (
    <div className="flex flex-1 items-center justify-center p-8">
      <div className="mx-auto max-w-md space-y-6 text-center">
        {repo.indexStatus === "NONE" && (
          <>
            <Database className="mx-auto size-12 text-muted-foreground" />
            <div className="space-y-2">
              <h2 className="font-heading text-lg font-semibold">Index this repository</h2>
              <p className="text-sm text-muted-foreground text-balance">
                Before you can chat, PlotTwist needs to index{" "}
                <strong>{repo.fullName}</strong> into the vector store.
              </p>
            </div>
            <div className="flex items-center justify-center gap-3">
              <Link
                href="/dashboard"
                className={cn(buttonVariants({ variant: "outline" }))}
              >
                <ArrowLeft className="size-4" />
                Dashboard
              </Link>
              <Button
                onClick={() => startIndex.mutate(repo.id)}
                disabled={startIndex.isPending}
              >
                <Database className="size-4" />
                Start indexing
              </Button>
            </div>
          </>
        )}

        {repo.indexStatus === "INDEXING" && (
          <>
            <div className="mx-auto size-12 animate-pulse rounded-full bg-primary/20 flex items-center justify-center">
              <Database className="size-6 text-primary" />
            </div>
            <div className="space-y-2">
              <h2 className="font-heading text-lg font-semibold">Indexing in progress…</h2>
              <p className="text-sm text-muted-foreground">
                {repo.filesProcessed}/{repo.filesTotal} files • {repo.chunkCount} chunks
              </p>
            </div>
            <Progress value={progress} className="h-2" />
          </>
        )}

        {repo.indexStatus === "FAILED" && (
          <>
            <AlertTriangle className="mx-auto size-12 text-destructive" />
            <div className="space-y-2">
              <h2 className="font-heading text-lg font-semibold">Indexing failed</h2>
              <p className="text-sm text-destructive">{repo.errorMessage}</p>
            </div>
            <Button
              onClick={() => startIndex.mutate(repo.id)}
              disabled={startIndex.isPending}
            >
              Retry indexing
            </Button>
          </>
        )}
      </div>
    </div>
  );
}
