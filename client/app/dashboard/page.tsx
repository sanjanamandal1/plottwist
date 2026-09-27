"use client";

import { RequireAuth } from "@/components/providers/require-auth";
import { AppShell } from "@/components/layout/app-shell";
import { RepoDashboard } from "@/components/dashboard/repo-dashboard";

export default function DashboardPage() {
  return (
    <RequireAuth>
      <AppShell title="Repositories" description="Your connected GitHub repos">
        <RepoDashboard />
      </AppShell>
    </RequireAuth>
  );
}
