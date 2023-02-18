import { ParticulateServer } from './lib/ParticulateServer';
"use strict";

ParticulateServer.start();

export const viteNodeApp = ParticulateServer.instance;