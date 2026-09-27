"use client";

import { Database, FolderGit2, MessageSquareCode, Sparkles } from "lucide-react";
import { useRepos } from "@/hooks/use-repos";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Spinner } from "@/components/ui/spinner";

export function OverviewDashboard() {
  const { data: repos, isLoading } = useRepos();

  if (isLoading) {
    return (
      <div className="flex flex-1 items-center justify-center p-8">
        <Spinner className="size-6" />
      </div>
    );
  }

  const totalRepos = repos?.length ?? 0;
  const indexed = repos?.filter((r) => r.indexStatus === "READY").length ?? 0;
  const totalChunks = repos?.reduce((sum, r) => sum + r.chunkCount, 0) ?? 0;
  const indexing = repos?.filter((r) => r.indexStatus === "INDEXING").length ?? 0;

  const stats = [
    { label: "Total Repos", value: totalRepos, icon: FolderGit2 },
    { label: "Indexed", value: indexed, icon: Database },
    { label: "Code Chunks", value: totalChunks.toLocaleString(), icon: Sparkles },
    { label: "Indexing Now", value: indexing, icon: MessageSquareCode },
  ];

  return (
    <div className="space-y-6 p-6">
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.label}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                {stat.label}
              </CardTitle>
              <stat.icon className="size-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <p className="text-2xl font-bold">{stat.value}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
