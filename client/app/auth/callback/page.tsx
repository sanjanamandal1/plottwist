"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "@/hooks/use-auth";
import { Spinner } from "@/components/ui/spinner";

/**
 * OAuth callback page — runs after Spring Security redirects back.
 * Sets a lightweight cookie so the Next.js middleware knows the user
 * is authenticated, then redirects to the dashboard.
 */
export default function AuthCallbackPage() {
  const { data: user, isLoading } = useCurrentUser();
  const router = useRouter();

  useEffect(() => {
    if (user) {
      // Set a cookie for the client-side middleware
      document.cookie = "plottwist_auth=1; path=/; max-age=604800; samesite=lax";
      router.replace("/dashboard");
    }
  }, [user, router]);

  return (
    <div className="flex min-h-svh items-center justify-center">
      <div className="flex flex-col items-center gap-3 text-muted-foreground">
        <Spinner className="size-8" />
        <p className="text-sm font-medium">Connecting your GitHub account…</p>
      </div>
    </div>
  );
}
