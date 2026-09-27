import type { SVGProps } from "react";

export function PlotTwistIcon(props: SVGProps<SVGSVGElement>) {
  return (
    <svg viewBox="0 0 64 64" fill="none" aria-hidden="true" {...props}>
      <defs>
        <linearGradient id="ptGrad" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" stopColor="#10b981" />
          <stop offset="50%" stopColor="#06b6d4" />
          <stop offset="100%" stopColor="#6366f1" />
        </linearGradient>
      </defs>
      <rect x="2" y="2" width="60" height="60" rx="18" fill="url(#ptGrad)" />
      {/* Twist ribbon design */}
      <path
        d="M20 44C20 32 44 32 44 20M44 44C44 32 20 32 20 20"
        stroke="white"
        strokeWidth="4.5"
        strokeLinecap="round"
      />
      <circle cx="20" cy="20" r="3.5" fill="white" />
      <circle cx="44" cy="44" r="3.5" fill="white" />
      <circle cx="44" cy="20" r="3.5" fill="white" />
      <circle cx="20" cy="44" r="3.5" fill="white" />
    </svg>
  );
}

// Backward compatibility alias
export { PlotTwistIcon as DevPilotIcon };
