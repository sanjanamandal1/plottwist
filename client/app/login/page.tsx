import Link from "next/link";
import { ArrowRight, FolderGit2, Shield } from "lucide-react";

import { PlotTwistIcon } from "@/components/icons/plottwist-icon";
import { BrandMark } from "@/components/layout/app-shell";
import { ModeToggle } from "@/components/ui/mode-toggle";
import { buttonVariants } from "@/components/ui/button";

import { cn } from "@/lib/utils";
import { getGithubLoginUrl } from "@/lib/api";

export default function LoginPage() {
  return (
    <div className="relative min-h-svh overflow-hidden bg-[#f7faf8] dark:bg-background">
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(ellipse_at_center,oklch(from_var(--primary)_l_c_h/0.1),transparent_60%)]" />

      <header className="relative z-10 mx-auto flex h-14 w-full max-w-5xl items-center justify-between px-4">
        <BrandMark />
        <ModeToggle />
      </header>

      <main className="relative z-10 mx-auto flex min-h-[calc(100svh-56px)] w-full max-w-md flex-col items-center justify-center gap-8 px-4">
        <div className="mx-auto flex size-20 items-center justify-center rounded-3xl bg-primary text-primary-foreground shadow-xl shadow-primary/25 ring-4 ring-primary/10">
          <PlotTwistIcon className="size-14 rounded-3xl" />
        </div>

        <div className="space-y-2 text-center">
          <h1 className="font-heading text-2xl font-semibold tracking-tight">
            Sign in to PlotTwist
          </h1>
          <p className="text-sm text-muted-foreground text-balance">
            Connect your GitHub account to start chatting with your codebase.
          </p>
        </div>

        <div className="w-full space-y-3">
          <a
            href={getGithubLoginUrl()}
            className={cn(
              buttonVariants({ size: "lg" }),
              "w-full inline-flex items-center justify-center gap-2"
            )}
          >
            <FolderGit2 className="size-5" />
            Continue with GitHub
            <ArrowRight className="size-4" />
          </a>
        </div>

        <div className="flex items-center gap-2 text-xs text-muted-foreground">
          <Shield className="size-3.5" />
          <span>We only request read access to your repositories.</span>
        </div>
      </main>
    </div>
  );
}
