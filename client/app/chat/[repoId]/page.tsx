"use client";

import { use } from "react";
import { RequireAuth } from "@/components/providers/require-auth";
import { ChatView } from "@/components/chat/chat-view";

export default function ChatPage({
  params,
}: {
  params: Promise<{ repoId: string }>;
}) {
  const { repoId } = use(params);

  return (
    <RequireAuth>
      <ChatView repoId={repoId} />
    </RequireAuth>
  );
}
