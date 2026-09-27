"use client";

import { useState } from "react";
import { useRepository } from "@/hooks/use-repos";
import { useChatSessions, useCreateChatSession, useChatMessages, useStreamChat } from "@/hooks/use-chat";
import { AppShell } from "@/components/layout/app-shell";
import { ChatSidebar } from "@/components/chat/chat-sidebar";
import { ChatMessages } from "@/components/chat/chat-messages";
import { ChatComposer } from "@/components/chat/chat-composer";
import { IndexingState } from "@/components/chat/indexing-state";
import { Spinner } from "@/components/ui/spinner";

export function ChatView({ repoId }: { repoId: string }) {
  const { data: repo, isLoading: repoLoading } = useRepository(repoId);
  const { data: sessions } = useChatSessions(repoId);
  const createSession = useCreateChatSession(repoId);

  const [activeSessionId, setActiveSessionId] = useState<string | null>(null);
  const { data: messages } = useChatMessages(activeSessionId);
  const { send, stop, streaming, streamText } = useStreamChat(activeSessionId);

  if (repoLoading) {
    return (
      <div className="flex min-h-svh items-center justify-center">
        <Spinner className="size-6" />
      </div>
    );
  }

  if (!repo) return null;

  // If repo isn't ready, show indexing state
  if (repo.indexStatus !== "READY") {
    return (
      <AppShell title={repo.fullName} description="Chat with your code">
        <IndexingState repo={repo} />
      </AppShell>
    );
  }

  const handleNewChat = async () => {
    const session = await createSession.mutateAsync();
    setActiveSessionId(session.id);
  };

  return (
    <AppShell title={repo.fullName} description="Chat with your code" hideHeader>
      <div className="flex h-[100svh]">
        {/* Sidebar */}
        <ChatSidebar
          repo={repo}
          sessions={sessions ?? []}
          activeSessionId={activeSessionId}
          onSelectSession={setActiveSessionId}
          onNewChat={handleNewChat}
          isCreating={createSession.isPending}
        />

        {/* Main chat area */}
        <div className="flex flex-1 flex-col">
          {/* Header */}
          <header className="flex h-14 shrink-0 items-center justify-between border-b bg-background/80 px-4 backdrop-blur">
            <div className="min-w-0">
              <h1 className="truncate text-sm font-medium">{repo.fullName}</h1>
              <p className="text-xs text-muted-foreground">
                {activeSessionId ? "Chat session active" : "Select or start a conversation"}
              </p>
            </div>
          </header>

          {/* Messages */}
          <div className="flex-1 overflow-y-auto">
            {activeSessionId ? (
              <ChatMessages
                messages={messages ?? []}
                streamText={streamText}
                streaming={streaming}
              />
            ) : (
              <div className="flex h-full items-center justify-center text-muted-foreground">
                <p className="text-sm">Start a new conversation to chat with this codebase.</p>
              </div>
            )}
          </div>

          {/* Composer */}
          {activeSessionId && (
            <ChatComposer
              onSend={send}
              onStop={stop}
              streaming={streaming}
            />
          )}
        </div>
      </div>
    </AppShell>
  );
}
