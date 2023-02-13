export const InterfaceColor = {
    RED: "bg-rose-700",
    YELLOW: "bg-yellow-500",
    ORANGE: "bg-orange-500",
    GREEN: "bg-lime-600",
    BLUE: "bg-cyan-500",
    PURPLE: "bg-fuchsia-600",
    GRAY: "bg-zinc-500",
    BLACK: "bg-slate-800",
    WHITE: "bg-neutral-300",
} as const;
export type InterfaceColor = typeof InterfaceColor[keyof typeof InterfaceColor];