import { requisitar } from "./cliente";
import type { DashboardResumo } from "./tipos";

export function buscarDashboard(): Promise<DashboardResumo> {
  return requisitar<DashboardResumo>("/api/dashboard");
}
