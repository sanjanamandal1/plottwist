import Link from "next/link";
import {
  ArrowRight,
  Bot,
  Code2,
  Cpu,
  FileCode2,
  FolderGit2,
  GitBranch,
  Layers,
  MessageSquareCode,
  Search,
  ShieldCheck,
  Sparkles,
  Zap,
} from "lucide-react";

import { PlotTwistIcon } from "@/components/icons/plottwist-icon";
import { BrandMark } from "@/components/layout/app-shell";
import { ModeToggle } from "@/components/ui/mode-toggle";
import { buttonVariants } from "@/components/ui/button";

import { cn } from "@/lib/utils";
import { getGithubLoginUrl } from "@/lib/api";

export default function HomePage() {
  return (
    <div className="relative min-h-svh overflow-hidden bg-background text-foreground selection:bg-primary/20">
      {/* Dynamic ambient background gradients */}
      <div className="pointer-events-none absolute -top-40 left-1/2 -z-10 h-[500px] w-[1000px] -translate-x-1/2 rounded-full bg-gradient-to-tr from-emerald-500/20 via-cyan-500/15 to-indigo-500/20 blur-[130px]" />
      <div className="pointer-events-none absolute top-1/2 right-0 -z-10 h-[400px] w-[500px] rounded-full bg-primary/10 blur-[100px]" />

      {/* Top Navbar */}
      <header className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/70 backdrop-blur-xl">
        <div className="mx-auto flex h-16 w-full max-w-6xl items-center justify-between px-4 sm:px-6">
          <BrandMark />
          <nav className="hidden items-center gap-6 text-sm font-medium text-muted-foreground md:flex">
            <a href="#features" className="transition-colors hover:text-foreground">
              Features
            </a>
            <a href="#architecture" className="transition-colors hover:text-foreground">
              Architecture
            </a>
            <a href="#security" className="transition-colors hover:text-foreground">
              Security
            </a>
          </nav>
          <div className="flex items-center gap-3">
            <ModeToggle />
            <Link
              href="/login"
              className={cn(buttonVariants({ variant: "ghost", size: "sm" }))}
            >
              Sign in
            </Link>
            <a
              href={getGithubLoginUrl()}
              className={cn(
                buttonVariants({ size: "sm" }),
                "hidden sm:inline-flex shadow-sm shadow-primary/25"
              )}
            >
              Get Started
              <ArrowRight className="size-3.5 ml-1" />
            </a>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <main className="relative z-10 mx-auto flex w-full max-w-6xl flex-col gap-24 px-4 py-16 sm:px-6 md:py-28">
        <section className="mx-auto max-w-3xl space-y-8 text-center">
          <div className="inline-flex items-center gap-2 rounded-full border border-border/70 bg-muted/60 px-3.5 py-1 text-xs font-medium text-foreground backdrop-blur-md">
            <span className="flex size-2 rounded-full bg-emerald-500 animate-pulse" />
            <span>PlotTwist Engine 2.0 • Intelligent Codebase RAG</span>
            <Sparkles className="size-3.5 text-primary" />
          </div>

          <div className="space-y-4">
            <h1 className="font-heading text-4xl font-extrabold tracking-tight sm:text-6xl md:text-7xl leading-[1.1]">
              Unravel the plot behind{" "}
              <span className="bg-gradient-to-r from-emerald-500 via-teal-500 to-indigo-600 bg-clip-text text-transparent">
                any codebase
              </span>
            </h1>
            <p className="mx-auto max-w-2xl text-lg text-muted-foreground sm:text-xl text-balance">
              Connect GitHub in seconds. PlotTwist parses AST-aware chunks, embeds your repository
              into high-dimensional vector space, and lets you interrogate complex code with instant,
              verified citations.
            </p>
          </div>

          <div className="flex flex-wrap items-center justify-center gap-4">
            <a
              href={getGithubLoginUrl()}
              className={cn(
                buttonVariants({ size: "lg" }),
                "inline-flex items-center gap-2 px-6 shadow-lg shadow-primary/25 hover:shadow-primary/40 transition-all text-base font-semibold"
              )}
            >
              <FolderGit2 className="size-5" />
              Connect GitHub Repository
              <ArrowRight className="size-4" />
            </a>
            <Link
              href="/login"
              className={cn(
                buttonVariants({ variant: "outline", size: "lg" }),
                "text-base backdrop-blur-md hover:bg-muted/80"
              )}
            >
              Explore Live Demo
            </Link>
          </div>

          {/* Quick Metrics */}
          <div className="pt-4 grid grid-cols-3 gap-4 border-t border-border/50 max-w-xl mx-auto">
            <div>
              <p className="text-2xl font-bold tracking-tight text-foreground">100%</p>
              <p className="text-xs text-muted-foreground font-medium">Grounded Citations</p>
            </div>
            <div>
              <p className="text-2xl font-bold tracking-tight text-foreground">&lt; 350ms</p>
              <p className="text-xs text-muted-foreground font-medium">Vector Lookup</p>
            </div>
            <div>
              <p className="text-2xl font-bold tracking-tight text-foreground">AES-256</p>
              <p className="text-xs text-muted-foreground font-medium">Token Encryption</p>
            </div>
          </div>
        </section>

        {/* Interactive Chat & Code Visual Preview */}
        <section className="relative mx-auto w-full max-w-5xl rounded-2xl border border-border/80 bg-card/60 p-2 shadow-2xl backdrop-blur-xl ring-1 ring-border/50">
          <div className="flex items-center justify-between border-b border-border/50 px-4 py-3">
            <div className="flex items-center gap-2">
              <span className="size-3 rounded-full bg-red-500/80" />
              <span className="size-3 rounded-full bg-amber-500/80" />
              <span className="size-3 rounded-full bg-emerald-500/80" />
              <span className="ml-2 text-xs font-mono text-muted-foreground">
                plottwist://workspace/architecture-query
              </span>
            </div>
            <div className="flex items-center gap-2 text-xs text-muted-foreground">
              <GitBranch className="size-3.5" />
              <span>main • synchronized</span>
            </div>
          </div>

          <div className="grid gap-4 p-4 md:grid-cols-12 md:p-6">
            <div className="md:col-span-7 space-y-4">
              <div className="flex items-start gap-3 rounded-xl bg-muted/40 p-4 border border-border/40">
                <div className="flex size-7 shrink-0 items-center justify-center rounded-lg bg-primary/20 text-primary font-bold text-xs">
                  YOU
                </div>
                <div className="space-y-1 text-sm">
                  <p className="font-semibold text-foreground">
                    How does the transaction rollback mechanism work in the payment service?
                  </p>
                </div>
              </div>

              <div className="flex items-start gap-3 rounded-xl bg-primary/5 p-4 border border-primary/20">
                <div className="flex size-7 shrink-0 items-center justify-center rounded-lg bg-primary text-primary-foreground font-bold text-xs">
                  <Bot className="size-4" />
                </div>
                <div className="space-y-2 text-sm text-foreground/90">
                  <p>
                    The rollback is handled via the <code className="text-xs font-mono bg-primary/10 text-primary px-1.5 py-0.5 rounded">PaymentOrchestrator.executeRefund()</code> pipeline which listens to failed saga events:
                  </p>
                  <div className="flex flex-wrap gap-2 pt-1">
                    <span className="inline-flex items-center gap-1 rounded-md border border-border/80 bg-background/80 px-2 py-0.5 text-xs font-mono text-muted-foreground">
                      <FileCode2 className="size-3 text-primary" />
                      src/services/PaymentOrchestrator.ts:L45-82
                    </span>
                    <span className="inline-flex items-center gap-1 rounded-md border border-border/80 bg-background/80 px-2 py-0.5 text-xs font-mono text-muted-foreground">
                      <FileCode2 className="size-3 text-primary" />
                      src/events/SagaHandler.ts:L112
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <div className="md:col-span-5 rounded-xl border border-border/50 bg-background/90 p-4 font-mono text-xs text-muted-foreground flex flex-col justify-between">
              <div>
                <div className="flex items-center justify-between pb-2 border-b border-border/40 mb-3 text-xs text-foreground">
                  <span className="flex items-center gap-1.5">
                    <Code2 className="size-3.5 text-emerald-500" />
                    Retrieved Code Chunk
                  </span>
                  <span className="text-[11px] text-emerald-600 font-semibold">Similarity: 0.94</span>
                </div>
                <pre className="overflow-x-auto text-[11px] leading-relaxed text-foreground/80">
{`async function rollbackSaga(ctx: SagaContext) {
  logger.warn("Initiating refund fallback", ctx.id);
  await ledger.markVoided(ctx.txnId);
  return gateway.reverseAuthorizedPayment({
    chargeId: ctx.chargeId,
    reason: "SAGA_ABORT_TIMEOUT"
  });
}`}
                </pre>
              </div>
              <div className="pt-3 border-t border-border/30 flex items-center justify-between text-[11px]">
                <span>pgvector cosine similarity</span>
                <span className="text-primary font-medium">Spring AI RAG</span>
              </div>
            </div>
          </div>
        </section>

        {/* Feature Grid */}
        <section id="features" className="space-y-12">
          <div className="text-center space-y-3">
            <h2 className="font-heading text-3xl font-bold tracking-tight sm:text-4xl">
              Engineered for developer clarity
            </h2>
            <p className="text-muted-foreground max-w-xl mx-auto text-balance">
              Everything you need to onboard onto legacy projects, audit architectures, or track down elusive bugs.
            </p>
          </div>

          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {[
              {
                title: "AST-Aware Chunking",
                desc: "Code isn't plain text. PlotTwist preserves class hierarchies, function boundaries, and module scopes.",
                icon: Layers,
              },
              {
                title: "pgvector RAG Store",
                desc: "High-performance vector embeddings matched in milliseconds with cosine similarity and metadata filters.",
                icon: Search,
              },
              {
                title: "Clickable Source Citations",
                desc: "Never take AI answers on faith. Jump straight to the exact line number in your repository.",
                icon: FileCode2,
              },
              {
                title: "Real-time SSE Streaming",
                desc: "Token-by-token responses streamed directly from Spring Boot to your browser with low latency.",
                icon: Zap,
              },
              {
                title: "AES-256 GCM Security",
                desc: "GitHub access tokens and sensitive credentials are encrypted at rest with industry-standard cryptography.",
                icon: ShieldCheck,
              },
              {
                title: "Multi-Model Intelligence",
                desc: "Pluggable LLM backends supported through Spring AI: OpenAI, Ollama, Anthropic, or local inference.",
                icon: Cpu,
              },
            ].map((f) => (
              <div
                key={f.title}
                className="group relative rounded-2xl border border-border/70 bg-card/60 p-6 shadow-sm backdrop-blur-md transition-all hover:-translate-y-1 hover:border-primary/40 hover:shadow-lg"
              >
                <div className="mb-4 inline-flex size-11 items-center justify-center rounded-xl bg-primary/10 text-primary transition-colors group-hover:bg-primary group-hover:text-primary-foreground">
                  <f.icon className="size-5" />
                </div>
                <h3 className="font-heading text-lg font-semibold text-foreground">{f.title}</h3>
                <p className="mt-2 text-sm text-muted-foreground leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </section>

        {/* Architecture Section */}
        <section id="architecture" className="rounded-3xl border border-border/80 bg-gradient-to-b from-muted/30 to-card/50 p-8 sm:p-12 backdrop-blur-xl">
          <div className="grid gap-8 lg:grid-cols-2 lg:items-center">
            <div className="space-y-4">
              <span className="text-xs font-semibold uppercase tracking-wider text-primary">
                Original Architecture
              </span>
              <h2 className="font-heading text-3xl font-bold tracking-tight sm:text-4xl">
                Modern Full-Stack Engineering
              </h2>
              <p className="text-muted-foreground leading-relaxed">
                PlotTwist couples a reactive Next.js 16 App Router frontend with a rock-solid Spring Boot 3 &amp; Spring AI backend, backed by PostgreSQL and pgvector.
              </p>
              <ul className="space-y-3 text-sm text-foreground/80">
                <li className="flex items-center gap-2">
                  <span className="size-1.5 rounded-full bg-primary" />
                  <strong>Frontend:</strong> Next.js 16 Turbopack, Tailwind CSS v4, Base UI, TanStack Query.
                </li>
                <li className="flex items-center gap-2">
                  <span className="size-1.5 rounded-full bg-primary" />
                  <strong>Backend:</strong> Java 21, Spring Boot, Spring Security OAuth2, Spring AI.
                </li>
                <li className="flex items-center gap-2">
                  <span className="size-1.5 rounded-full bg-primary" />
                  <strong>Database:</strong> PostgreSQL 16 with pgvector extension enabled.
                </li>
              </ul>
            </div>

            <div className="rounded-2xl border border-border/60 bg-background/80 p-6 shadow-inner font-mono text-xs space-y-3">
              <div className="text-muted-foreground pb-2 border-b border-border/40">
                // Pipeline Execution Trace
              </div>
              <div className="space-y-2">
                <div className="flex items-center gap-2 text-emerald-600 dark:text-emerald-400">
                  <span>✔</span>
                  <span>[OAuth2] GitHub Token Verified &amp; Encrypted</span>
                </div>
                <div className="flex items-center gap-2 text-emerald-600 dark:text-emerald-400">
                  <span>✔</span>
                  <span>[Crawler] GitHub API Tree Fetched (Filtered binary/lockfiles)</span>
                </div>
                <div className="flex items-center gap-2 text-emerald-600 dark:text-emerald-400">
                  <span>✔</span>
                  <span>[Chunker] Code Chunked by semantic declarations (800 tokens)</span>
                </div>
                <div className="flex items-center gap-2 text-emerald-600 dark:text-emerald-400">
                  <span>✔</span>
                  <span>[Embedding] Spring AI VectorStore &amp; pgvector Ingestion</span>
                </div>
                <div className="flex items-center gap-2 text-indigo-600 dark:text-indigo-400">
                  <span>➜</span>
                  <span>[Query] User prompt augmented with Top-5 Similarity Docs</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Bottom CTA */}
        <section className="text-center space-y-6 py-8">
          <h2 className="font-heading text-3xl font-bold tracking-tight sm:text-4xl">
            Ready to understand your code at lightspeed?
          </h2>
          <p className="text-muted-foreground max-w-lg mx-auto">
            Connect your repository and start chatting with PlotTwist now.
          </p>
          <div>
            <a
              href={getGithubLoginUrl()}
              className={cn(
                buttonVariants({ size: "lg" }),
                "inline-flex items-center gap-2 px-8 shadow-xl shadow-primary/25 text-base font-semibold"
              )}
            >
              <FolderGit2 className="size-5" />
              Sign in with GitHub
              <ArrowRight className="size-4" />
            </a>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="border-t border-border/40 bg-background/50 py-8">
        <div className="mx-auto flex max-w-6xl flex-col items-center justify-between gap-4 px-4 sm:flex-row sm:px-6">
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <PlotTwistIcon className="size-5 rounded-md" />
            <span>PlotTwist • Codebase Intelligence Engine</span>
          </div>
          <p className="text-xs text-muted-foreground">
            Built for modern engineering teams. Powered by Spring AI &amp; Next.js.
          </p>
        </div>
      </footer>
    </div>
  );
}
