"use client";

import Link from "next/link";
import { ArrowLeft, MessageSquarePlus, MessageSquare } from "lucide-react";

import { type ChatSession, type Repository } from "@/lib/api";
import { cn } from "@/lib/utils";
import { Button, buttonVariants } from "@/components/ui/button";

type ChatSidebarProps = {
  repo: Repository;
  sessions: ChatSession[];
  activeSessionId: string | null;
  onSelectSession: (id: string) => void;
  onNewChat: () => void;
  isCreating: boolean;
};

export function ChatSidebar({
  repo,
  sessions,
  activeSessionId,
  onSelectSession,
  onNewChat,
  isCreating,
}: ChatSidebarProps) {
  return (
    <aside className="flex w-64 shrink-0 flex-col border-r bg-muted/30">
      {/* Back to dashboard */}
      <div className="flex h-14 items-center gap-2 border-b px-3">
        <Link
          href="/dashboard"
          className={cn(buttonVariants({ variant: "ghost", size: "icon" }), "size-8")}
        >
          <ArrowLeft className="size-4" />
        </Link>
        <span className="truncate text-sm font-medium">{repo.name}</span>
      </div>

      {/* New chat button */}
      <div className="p-3">
        <Button
          onClick={onNewChat}
          disabled={isCreating}
          size="sm"
          className="w-full"
        >
          <MessageSquarePlus className="size-4" />
          New conversation
        </Button>
      </div>

      {/* Session list */}
      <div className="flex-1 overflow-y-auto px-2 pb-2">
        {sessions.length === 0 ? (
          <p className="px-2 py-4 text-center text-xs text-muted-foreground">
            No conversations yet.
          </p>
        ) : (
          <ul className="space-y-0.5">
            {sessions.map((session) => (
              <li key={session.id}>
                <button
                  onClick={() => onSelectSession(session.id)}
                  className={cn(
                    "flex w-full items-center gap-2 rounded-md px-2.5 py-2 text-left text-sm transition-colors",
                    "hover:bg-accent",
                    activeSessionId === session.id &&
                      "bg-accent text-accent-foreground font-medium"
                  )}
                >
                  <MessageSquare className="size-3.5 shrink-0" />
                  <span className="truncate">{session.title}</span>
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </aside>
  );
}
