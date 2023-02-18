import { ErrorEvent } from "ws";

export type OnErrorCallback = (event: ErrorEvent) => void;