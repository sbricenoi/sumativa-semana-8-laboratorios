import { Injectable } from '@angular/core';
import { Usuario } from '../../shared/models/usuario.model';
import { Laboratorio, TipoAnalisis } from '../../shared/models/laboratorio.model';
import { Cita } from '../../shared/models/cita.model';
import { Resultado } from '../../shared/models/resultado.model';

@Injectable({
  providedIn: 'root'
})
export class MockDataService {
  private readonly USUARIOS_KEY = 'usuarios_mock';
  private readonly LABORATORIOS_KEY = 'laboratorios_mock';
  private readonly ANALISIS_KEY = 'analisis_mock';
  private readonly CITAS_KEY = 'citas_mock';
  private readonly RESULTADOS_KEY = 'resultados_mock';

  constructor() {
    this.initializeData();
  }

  private initializeData(): void {
    if (!localStorage.getItem(this.USUARIOS_KEY)) {
      localStorage.setItem(this.USUARIOS_KEY, JSON.stringify(this.getDefaultUsuarios()));
    }
    if (!localStorage.getItem(this.LABORATORIOS_KEY)) {
      localStorage.setItem(this.LABORATORIOS_KEY, JSON.stringify(this.getDefaultLaboratorios()));
    }
    if (!localStorage.getItem(this.ANALISIS_KEY)) {
      localStorage.setItem(this.ANALISIS_KEY, JSON.stringify(this.getDefaultAnalisis()));
    }
    if (!localStorage.getItem(this.CITAS_KEY)) {
      localStorage.setItem(this.CITAS_KEY, JSON.stringify(this.getDefaultCitas()));
    }
    if (!localStorage.getItem(this.RESULTADOS_KEY)) {
      localStorage.setItem(this.RESULTADOS_KEY, JSON.stringify(this.getDefaultResultados()));
    }
  }

  private getDefaultUsuarios(): Usuario[] {
    return [
      {
        idUsuario: 1,
        nombre: 'Admin',
        apellido: 'Sistema',
        email: 'admin@lab.cl',
        password: '$2a$10$password123', // En producción: hasheado
        rol: 'ADMINISTRADOR',
        fechaCreacion: new Date(),
        activo: true
      },
      {
        idUsuario: 2,
        nombre: 'María',
        apellido: 'González',
        email: 'maria@email.cl',
        password: '$2a$10$password123',
        rol: 'PACIENTE',
        fechaCreacion: new Date(),
        activo: true
      },
      {
        idUsuario: 3,
        nombre: 'Carlos',
        apellido: 'López',
        email: 'carlos@lab.cl',
        password: '$2a$10$password123',
        rol: 'LABORATORISTA',
        fechaCreacion: new Date(),
        activo: true
      },
      {
        idUsuario: 4,
        nombre: 'Ana',
        apellido: 'Martínez',
        email: 'ana@hospital.cl',
        password: '$2a$10$password123',
        rol: 'MEDICO',
        fechaCreacion: new Date(),
        activo: true
      },
      {
        idUsuario: 5,
        nombre: 'Pedro',
        apellido: 'Silva',
        email: 'pedro@email.cl',
        password: '$2a$10$password123',
        rol: 'PACIENTE',
        fechaCreacion: new Date(),
        activo: true
      }
    ];
  }

  private getDefaultLaboratorios(): Laboratorio[] {
    return [
      {
        idLaboratorio: 1,
        nombre: 'LabCorp Chile',
        direccion: 'Av. Providencia 1234, Santiago',
        telefono: '+56-2-2345-6789',
        email: 'contacto@labcorp.cl',
        especialidad: 'Hematología',
        activo: true,
        fechaCreacion: new Date()
      },
      {
        idLaboratorio: 2,
        nombre: 'Clínica Alemana Laboratorios',
        direccion: 'Av. Vitacura 5951, Santiago',
        telefono: '+56-2-2210-1111',
        email: 'lab@alemana.cl',
        especialidad: 'Microbiología',
        activo: true,
        fechaCreacion: new Date()
      },
      {
        idLaboratorio: 3,
        nombre: 'Megasalud Diagnóstico',
        direccion: 'Av. Apoquindo 4500, Las Condes',
        telefono: '+56-2-2654-3210',
        email: 'info@megasalud.cl',
        especialidad: 'Química Clínica',
        activo: true,
        fechaCreacion: new Date()
      },
      {
        idLaboratorio: 4,
        nombre: 'Laboratorio Médico Integrado',
        direccion: 'Calle Las Tranqueras 567, Vitacura',
        telefono: '+56-2-2789-1234',
        email: 'contacto@labintegrado.cl',
        especialidad: 'Medicina Integral',
        activo: true,
        fechaCreacion: new Date()
      }
    ];
  }

  private getDefaultAnalisis(): TipoAnalisis[] {
    return [
      {
        idTipoAnalisis: 1,
        nombre: 'Hemograma Completo',
        descripcion: 'Análisis completo de células sanguíneas',
        precio: 15000,
        tiempoEntregaDias: 1,
        activo: true
      },
      {
        idTipoAnalisis: 2,
        nombre: 'Perfil Bioquímico',
        descripcion: 'Glucosa, colesterol, triglicéridos, función hepática y renal',
        precio: 25000,
        tiempoEntregaDias: 2,
        activo: true
      },
      {
        idTipoAnalisis: 3,
        nombre: 'Examen de Orina Completo',
        descripcion: 'Análisis físico, químico y microscópico de orina',
        precio: 12000,
        tiempoEntregaDias: 1,
        activo: true
      },
      {
        idTipoAnalisis: 4,
        nombre: 'Perfil Tiroideo',
        descripcion: 'TSH, T3, T4 libre',
        precio: 35000,
        tiempoEntregaDias: 3,
        activo: true
      },
      {
        idTipoAnalisis: 5,
        nombre: 'PCR para COVID-19',
        descripcion: 'Test PCR para detección de SARS-CoV-2',
        precio: 30000,
        tiempoEntregaDias: 1,
        activo: true
      },
      {
        idTipoAnalisis: 6,
        nombre: 'Perfil Lipídico',
        descripcion: 'Colesterol total, HDL, LDL, triglicéridos',
        precio: 20000,
        tiempoEntregaDias: 2,
        activo: true
      }
    ];
  }

  private getDefaultCitas(): Cita[] {
    return [
      {
        idCita: 1,
        idPaciente: 2,
        nombrePaciente: 'María González',
        idLaboratorio: 1,
        nombreLaboratorio: 'LabCorp Chile',
        idTipoAnalisis: 1,
        nombreAnalisis: 'Hemograma Completo',
        fechaCita: new Date('2025-11-25T09:00:00'),
        estado: 'PROGRAMADA',
        observaciones: 'Ayuno de 8 horas',
        fechaCreacion: new Date()
      },
      {
        idCita: 2,
        idPaciente: 2,
        nombrePaciente: 'María González',
        idLaboratorio: 2,
        nombreLaboratorio: 'Clínica Alemana Laboratorios',
        idTipoAnalisis: 4,
        nombreAnalisis: 'Perfil Tiroideo',
        fechaCita: new Date('2025-11-26T10:30:00'),
        estado: 'CONFIRMADA',
        observaciones: 'Control de tiroides',
        fechaCreacion: new Date()
      },
      {
        idCita: 3,
        idPaciente: 5,
        nombrePaciente: 'Pedro Silva',
        idLaboratorio: 3,
        nombreLaboratorio: 'Megasalud Diagnóstico',
        idTipoAnalisis: 5,
        nombreAnalisis: 'PCR para COVID-19',
        fechaCita: new Date('2025-11-24T14:00:00'),
        estado: 'COMPLETADA',
        observaciones: 'Test PCR urgente',
        fechaCreacion: new Date()
      }
    ];
  }

  private getDefaultResultados(): Resultado[] {
    return [
      {
        idResultado: 1,
        idCita: 3,
        idLaboratorista: 3,
        nombreLaboratorista: 'Carlos López',
        nombrePaciente: 'Pedro Silva',
        nombreAnalisis: 'PCR para COVID-19',
        archivoPdf: '/resultados/2025/11/resultado_001.pdf',
        observaciones: 'Resultado negativo para SARS-CoV-2',
        fechaResultado: new Date('2025-11-24T18:00:00'),
        estado: 'COMPLETADO',
        valoresMedidos: '{"resultado": "NEGATIVO", "ct_value": "N/A"}',
        fechaCreacion: new Date()
      }
    ];
  }

  // Métodos públicos para obtener datos
  getUsuarios(): Usuario[] {
    const data = localStorage.getItem(this.USUARIOS_KEY);
    return data ? JSON.parse(data) : [];
  }

  getLaboratorios(): Laboratorio[] {
    const data = localStorage.getItem(this.LABORATORIOS_KEY);
    return data ? JSON.parse(data) : [];
  }

  getAnalisis(): TipoAnalisis[] {
    const data = localStorage.getItem(this.ANALISIS_KEY);
    return data ? JSON.parse(data) : [];
  }

  getCitas(): Cita[] {
    const data = localStorage.getItem(this.CITAS_KEY);
    return data ? JSON.parse(data) : [];
  }

  getResultados(): Resultado[] {
    const data = localStorage.getItem(this.RESULTADOS_KEY);
    return data ? JSON.parse(data) : [];
  }

  // Métodos para guardar datos actualizados
  saveUsuarios(usuarios: Usuario[]): void {
    localStorage.setItem(this.USUARIOS_KEY, JSON.stringify(usuarios));
  }

  saveLaboratorios(laboratorios: Laboratorio[]): void {
    localStorage.setItem(this.LABORATORIOS_KEY, JSON.stringify(laboratorios));
  }

  saveAnalisis(analisis: TipoAnalisis[]): void {
    localStorage.setItem(this.ANALISIS_KEY, JSON.stringify(analisis));
  }

  saveCitas(citas: Cita[]): void {
    localStorage.setItem(this.CITAS_KEY, JSON.stringify(citas));
  }

  saveResultados(resultados: Resultado[]): void {
    localStorage.setItem(this.RESULTADOS_KEY, JSON.stringify(resultados));
  }

  // Método para resetear todos los datos
  resetAllData(): void {
    localStorage.removeItem(this.USUARIOS_KEY);
    localStorage.removeItem(this.LABORATORIOS_KEY);
    localStorage.removeItem(this.ANALISIS_KEY);
    localStorage.removeItem(this.CITAS_KEY);
    localStorage.removeItem(this.RESULTADOS_KEY);
    this.initializeData();
  }
}

