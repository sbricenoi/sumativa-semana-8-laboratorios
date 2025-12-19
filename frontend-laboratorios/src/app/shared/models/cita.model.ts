export interface Cita {
  idCita?: number;
  idPaciente: number;
  nombrePaciente?: string;
  idLaboratorio: number;
  nombreLaboratorio?: string;
  idTipoAnalisis: number;
  nombreAnalisis?: string;
  fechaCita: Date | string;
  estado: 'PROGRAMADA' | 'CONFIRMADA' | 'COMPLETADA' | 'CANCELADA';
  observaciones?: string;
  fechaCreacion?: Date;
}

export interface CrearCitaRequest {
  idPaciente: number;
  idLaboratorio: number;
  idTipoAnalisis: number;
  fechaCita: string;
  observaciones?: string;
}

