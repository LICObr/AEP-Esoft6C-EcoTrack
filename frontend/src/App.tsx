import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { Header } from './components/Header'
import { AdminPontos } from './pages/AdminPontos'
import { Dashboard } from './pages/Dashboard'
import { MeuHistorico } from './pages/MeuHistorico'
import { PontosColeta } from './pages/PontosColeta'
import { RegistrarDescarte } from './pages/RegistrarDescarte'

export function App() {
  return (
    <BrowserRouter>
      <Header />
      <main className="conteudo-principal">
        <Routes>
          <Route path="/" element={<PontosColeta />} />
          <Route path="/registrar" element={<RegistrarDescarte />} />
          <Route path="/historico" element={<MeuHistorico />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/admin" element={<AdminPontos />} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}
