import '@testing-library/jest-dom/vitest'
import { cleanup } from '@testing-library/react'
import { afterEach } from 'vitest'

// sem globals o testing library nao limpa a tela sozinho
afterEach(() => cleanup())
