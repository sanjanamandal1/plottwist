"use client";

import { FileCode2 } from "lucide-react";
import { type Citation } from "@/lib/api";

export function CitationChips({ citations }: { citations: Citation[] }) {
  if (!citations.length) return null;

  return (
    <div className="flex flex-wrap gap-1.5">
      {citations.map((cite, i) => (
        <span
          key={i}
          title={cite.snippet}
          className="inline-flex items-center gap-1 rounded-md bg-primary/10 px-2 py-0.5 text-[11px] font-medium text-primary"
        >
          <FileCode2 className="size-3" />
          {cite.filePath.split("/").pop()}
        </span>
      ))}
    </div>
  );
}
