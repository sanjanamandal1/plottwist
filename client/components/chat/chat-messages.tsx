"use client";

import { useEffect, useRef } from "react";
import { Bot, User } from "lucide-react";

import { type ChatMessage } from "@/lib/api";
import { cn } from "@/lib/utils";
import { ChatMarkdown } from "@/components/chat/chat-markdown";
import { CitationChips } from "@/components/chat/citation-chips";

type ChatMessagesProps = {
  messages: ChatMessage[];
  streamText: string;
  streaming: boolean;
};

export function ChatMessages({ messages, streamText, streaming }: ChatMessagesProps) {
  const endRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages.length, streamText]);

  return (
    <div className="mx-auto max-w-3xl space-y-1 px-4 py-6">
      {messages.map((msg) => (
        <MessageBubble key={msg.id} message={msg} />
      ))}

      {/* Streaming in-progress message */}
      {streaming && streamText && (
        <div className="flex gap-3 py-3">
          <div className="flex size-7 shrink-0 items-center justify-center rounded-full bg-primary text-primary-foreground">
            <Bot className="size-4" />
          </div>
          <div className="prose prose-sm dark:prose-invert max-w-none flex-1">
            <ChatMarkdown content={streamText} />
            <span className="inline-block size-2 animate-pulse rounded-full bg-primary" />
          </div>
        </div>
      )}

      <div ref={endRef} />
    </div>
  );
}

function MessageBubble({ message }: { message: ChatMessage }) {
  const isUser = message.role === "USER";

  return (
    <div className={cn("flex gap-3 py-3", isUser && "flex-row-reverse")}>
      <div
        className={cn(
          "flex size-7 shrink-0 items-center justify-center rounded-full",
          isUser
            ? "bg-muted text-muted-foreground"
            : "bg-primary text-primary-foreground"
        )}
      >
        {isUser ? <User className="size-4" /> : <Bot className="size-4" />}
      </div>

      <div
        className={cn(
          "max-w-[80%] space-y-2",
          isUser && "text-right"
        )}
      >
        <div
          className={cn(
            "inline-block rounded-2xl px-4 py-2.5 text-sm",
            isUser
              ? "bg-primary text-primary-foreground rounded-br-md"
              : "bg-muted rounded-bl-md"
          )}
        >
          {isUser ? (
            <p className="whitespace-pre-wrap">{message.content}</p>
          ) : (
            <div className="prose prose-sm dark:prose-invert max-w-none">
              <ChatMarkdown content={message.content} />
            </div>
          )}
        </div>

        {!isUser && message.citations.length > 0 && (
          <CitationChips citations={message.citations} />
        )}
      </div>
    </div>
  );
}
