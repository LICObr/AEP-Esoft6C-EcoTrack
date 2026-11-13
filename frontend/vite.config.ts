import react from '@vitejs/plugin-react'
import { defineConfig } from 'vitest/config'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    environment: 'jsdom',
    setupFiles: './src/setupTestes.ts',
    coverage: {
      provider: 'v8',
      include: ['src/**/*.{ts,tsx}'],
      exclude: ['src/**/*.test.{ts,tsx}', 'src/setupTestes.ts', 'src/main.tsx', 'src/api/tipos.ts'],
      reporter: ['text', 'html', 'json-summary'],
      // abaixo disso o comando falha, igual o jacoco no backend
      thresholds: { lines: 70, branches: 70, functions: 70, statements: 70 },
    },
  },
})
