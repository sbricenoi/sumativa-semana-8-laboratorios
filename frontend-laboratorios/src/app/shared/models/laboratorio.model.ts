export interface Laboratorio {
  idLaboratorio?: number;
  nombre: string;
  direccion: string;
  telefono: string;
  email: string;
  especialidad: string;
  activo?: boolean;
  fechaCreacion?: Date;
  analisisDisponibles?: TipoAnalisis[];
}

export interface TipoAnalisis {
  idTipoAnalisis?: number;
  nombre: string;
  descripcion: string;
  precio: number;
  tiempoEntregaDias: number;
  activo?: boolean;
}

export interface LaboratorioAnalisis {
  idLaboratorio: number;
  idTipoAnalisis: number;
  disponible: boolean;
}

