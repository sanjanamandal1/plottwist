"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";

import { api } from "@/lib/api";
import { queryKeys } from "@/lib/query-keys";

export function useCurrentUser() {
  return useQuery({
    queryKey: queryKeys.auth.me(),
    queryFn: api.me,
    retry: false,
    staleTime: 60_000,
  });
}

export function useLogout() {
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation({
    mutationFn: api.logout,
    onSuccess: () => {
      // Clear the lightweight auth cookie
      document.cookie =
        "plottwist_auth=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
      queryClient.clear();
      router.push("/");
    },
  });
}
