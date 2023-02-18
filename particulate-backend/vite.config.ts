import { defineConfig } from 'vite';
import { resolve } from 'path';
import { nodeResolve } from '@rollup/plugin-node-resolve';
import { copy } from '@web/rollup-plugin-copy';
import { VitePluginNode } from 'vite-plugin-node';
import cleanPlugin from 'vite-plugin-clean';

// https://vitejs.dev/config/
export default defineConfig({
    build: {
        lib: {
          // Could also be a dictionary or array of multiple entry points
          entry: resolve(__dirname, 'index.ts'),
          name: 'particulate-backend',
          // the proper extensions will be added
          fileName: 'particulate-backend',
        },
        rollupOptions: {
            plugins: [
                nodeResolve({
                    browser: false,

                }),
            ],
        },
    },
    plugins: [
        copy({
            patterns: '**/assets/**',
            exclude: [],
            rootDir: undefined
        }),
        VitePluginNode({
            appPath: './index.ts',
            adapter: 'express'
        }),
        (cleanPlugin as any)({
            targetFiles: ['dist', 'test']
        })
    ],
})
