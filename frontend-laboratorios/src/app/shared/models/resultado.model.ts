export interface Resultado {
  idResultado?: number;
  idCita: number;
  idLaboratorista: number;
  nombreLaboratorista?: string;
  nombrePaciente?: string;
  nombreAnalisis?: string;
  archivoPdf?: string;
  observaciones?: string;
  fechaResultado: Date | string;
  estado: 'PENDIENTE' | 'EN_PROCESO' | 'COMPLETADO' | 'REVISADO';
  valoresMedidos?: string;
  fechaCreacion?: Date;
}

