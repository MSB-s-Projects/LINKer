import tailwindcss from '@tailwindcss/vite';
import { TanStackRouterVite } from "@tanstack/router-plugin/vite";
import react from '@vitejs/plugin-react-swc';
import path from 'path';
import { defineConfig } from 'vite';


// https://vite.dev/config/
export default defineConfig({
	plugins: [TanStackRouterVite({ autoCodeSplitting: true }), react(), tailwindcss()],
	resolve: {
		alias: {
			'@': path.resolve(__dirname, './src'),
		},
	},
})
