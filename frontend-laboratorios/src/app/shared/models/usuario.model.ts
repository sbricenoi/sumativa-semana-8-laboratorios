export interface Usuario {
  idUsuario?: number;
  nombre: string;
  apellido: string;
  email: string;
  password?: string;
  rol: 'ADMINISTRADOR' | 'PACIENTE' | 'LABORATORISTA' | 'MEDICO';
  fechaCreacion?: Date;
  activo?: boolean;
  token?: string; // Token JWT
}

export interface LoginRequest {
  email: string;
  password: string;
}

// LoginResponse del backend (directamente los campos del usuario)
export interface LoginResponse {
  idUsuario: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: 'ADMINISTRADOR' | 'PACIENTE' | 'LABORATORISTA' | 'MEDICO';
  token: string; // Token JWT
  mensaje: string;
}

export interface RegistroRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  rol: string;
  telefono?: string;
}

