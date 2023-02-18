import * as fs from 'fs';

export class KeyLoader {
    public static getPublic = () => fs.readFileSync('./public.key', 'utf8');

    public static getPrivate = () => fs.readFileSync('./private.key', 'utf8');
}