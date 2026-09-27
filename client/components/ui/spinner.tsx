import { cn } from "@/lib/utils"
import { HugeiconsIcon } from "@hugeicons/react"
import { Loading03Icon } from "@hugeicons/core-free-icons"

export interface SpinnerProps extends React.HTMLAttributes<HTMLSpanElement> {
  size?: number;
}

function Spinner({ className, size = 16, ...props }: SpinnerProps) {
  return (
    <span
      data-slot="spinner"
      role="status"
      aria-label="Loading"
      className={cn("inline-flex items-center justify-center animate-spin", className)}
      {...props}
    >
      <HugeiconsIcon
        icon={Loading03Icon}
        strokeWidth={2}
        size={size}
      />
    </span>
  );
}

export { Spinner }
