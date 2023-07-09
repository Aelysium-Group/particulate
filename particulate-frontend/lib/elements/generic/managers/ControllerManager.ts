import { Control } from "../controls/Control";
import { Manager } from "./Manager";
import { OrderedManager } from "./OrderedManager";

export class ControllerManager extends OrderedManager<string, JSX.Element> {}