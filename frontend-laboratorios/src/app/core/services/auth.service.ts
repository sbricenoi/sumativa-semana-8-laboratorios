import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { delay, map, catchError, tap } from 'rxjs/operators';
import { Usuario, LoginRequest, LoginResponse, RegistroRequest } from '../../shared/models/usuario.model';
import { MockDataService } from './mock-data.service';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../shared/models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly STORAGE_KEY = 'currentUser';
  private readonly API_URL = environment.apiUrl.usuarios;
  private currentUserSubject: BehaviorSubject<Usuario | null>;
  public currentUser$: Observable<Usuario | null>;
  private useMockData = false; // Cambia a true para usar datos simulados

  constructor(
    private http: HttpClient,
    private mockDataService: MockDataService
  ) {
    const savedUser = localStorage.getItem(this.STORAGE_KEY);
    this.currentUserSubject = new BehaviorSubject<Usuario | null>(
      savedUser ? JSON.parse(savedUser) : null
    );
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    if (this.useMockData) {
      return this.loginMock(request);
    }

    // Login real con backend
    return this.http.post<ApiResponse<LoginResponse>>(`${this.API_URL}/usuarios/login`, request)
      .pipe(
        map(response => {
          const loginData = response.data; // LoginResponse con los campos directos
          
          // Convertir LoginResponse a Usuario e incluir el token
          const usuario: Usuario = {
            idUsuario: loginData.idUsuario,
            nombre: loginData.nombre,
            apellido: loginData.apellido,
            email: loginData.email,
            rol: loginData.rol,
            token: loginData.token, // Guardar token JWT
            activo: true
          };

          console.log('Usuario logueado:', usuario);
          
          localStorage.setItem(this.STORAGE_KEY, JSON.stringify(usuario));
          this.currentUserSubject.next(usuario);

          return loginData; // Retornar LoginResponse original
        }),
        catchError(error => {
          console.error('Error en login:', error);
          return throwError(() => new Error(error.error?.message || 'Error al iniciar sesión'));
        })
      );
  }

  private loginMock(request: LoginRequest): Observable<LoginResponse> {
    const usuarios = this.mockDataService.getUsuarios();
    const usuario = usuarios.find(u => 
      u.email === request.email && u.activo
    );

    if (!usuario) {
      return throwError(() => new Error('Usuario no encontrado o inactivo'));
    }

    const usuarioSinPassword = { ...usuario };
    delete usuarioSinPassword.password;

    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(usuarioSinPassword));
    this.currentUserSubject.next(usuarioSinPassword);

    const response: LoginResponse = {
      idUsuario: usuario.idUsuario!,
      nombre: usuario.nombre,
      apellido: usuario.apellido,
      email: usuario.email,
      rol: usuario.rol,
      mensaje: 'Login exitoso',
      token: 'mock-jwt-token-' + Date.now() // Token simulado para modo mock
    };

    return of(response).pipe(delay(500));
  }

  register(request: RegistroRequest): Observable<Usuario> {
    if (this.useMockData) {
      return this.registerMock(request);
    }

    // Registro real con backend
    return this.http.post<ApiResponse<Usuario>>(`${this.API_URL}/usuarios/registro`, request)
      .pipe(
        map(response => {
          const usuario = response.data;
          const usuarioSinPassword = { ...usuario };
          delete usuarioSinPassword.password;
          return usuarioSinPassword;
        }),
        catchError(error => {
          console.error('Error en registro:', error);
          return throwError(() => new Error(error.error?.message || 'Error al registrar usuario'));
        })
      );
  }

  private registerMock(request: RegistroRequest): Observable<Usuario> {
    const usuarios = this.mockDataService.getUsuarios();
    
    if (usuarios.some(u => u.email === request.email)) {
      return throwError(() => new Error('El email ya está registrado'));
    }

    const nuevoUsuario: Usuario = {
      idUsuario: this.getNextId(usuarios),
      nombre: request.nombre,
      apellido: request.apellido,
      email: request.email,
      password: request.password,
      rol: request.rol as any,
      fechaCreacion: new Date(),
      activo: true
    };

    usuarios.push(nuevoUsuario);
    this.mockDataService.saveUsuarios(usuarios);

    const usuarioSinPassword = { ...nuevoUsuario };
    delete usuarioSinPassword.password;

    return of(usuarioSinPassword).pipe(delay(500));
  }

  logout(): void {
    localStorage.removeItem(this.STORAGE_KEY);
    this.currentUserSubject.next(null);
  }

  recuperarPassword(email: string): Observable<{message: string}> {
    const usuarios = this.mockDataService.getUsuarios();
    const usuario = usuarios.find(u => u.email === email);

    if (!usuario) {
      return throwError(() => new Error('Usuario no encontrado'));
    }

    // Simular envío de email
    return of({
      message: 'Se ha enviado un código de verificación a tu email'
    }).pipe(delay(1000));
  }

  verificarCodigo(codigo: string): Observable<boolean> {
    // Simular verificación de código (siempre acepta "123456")
    return of(codigo === '123456').pipe(delay(500));
  }

  resetPassword(email: string, nuevaPassword: string): Observable<{message: string}> {
    const usuarios = this.mockDataService.getUsuarios();
    const usuarioIndex = usuarios.findIndex(u => u.email === email);

    if (usuarioIndex === -1) {
      return throwError(() => new Error('Usuario no encontrado'));
    }

    usuarios[usuarioIndex].password = nuevaPassword; // En producción: hashear
    this.mockDataService.saveUsuarios(usuarios);

    return of({
      message: 'Contraseña actualizada exitosamente'
    }).pipe(delay(500));
  }

  actualizarPerfil(idUsuario: number, datos: Partial<Usuario>): Observable<Usuario> {
    if (this.useMockData) {
      return this.actualizarPerfilMock(idUsuario, datos);
    }

    // Actualización real con backend
    return this.http.put<ApiResponse<Usuario>>(`${this.API_URL}/usuarios/${idUsuario}`, datos)
      .pipe(
        map(response => {
          const usuario = response.data;
          const usuarioSinPassword = { ...usuario };
          delete usuarioSinPassword.password;

          // Actualizar usuario actual si es el mismo
          if (this.currentUserValue?.idUsuario === idUsuario) {
            localStorage.setItem(this.STORAGE_KEY, JSON.stringify(usuarioSinPassword));
            this.currentUserSubject.next(usuarioSinPassword);
          }

          return usuarioSinPassword;
        }),
        catchError(error => {
          console.error('Error al actualizar perfil:', error);
          return throwError(() => new Error(error.error?.message || 'Error al actualizar perfil'));
        })
      );
  }

  private actualizarPerfilMock(idUsuario: number, datos: Partial<Usuario>): Observable<Usuario> {
    const usuarios = this.mockDataService.getUsuarios();
    const usuarioIndex = usuarios.findIndex(u => u.idUsuario === idUsuario);

    if (usuarioIndex === -1) {
      return throwError(() => new Error('Usuario no encontrado'));
    }

    usuarios[usuarioIndex] = { ...usuarios[usuarioIndex], ...datos };
    this.mockDataService.saveUsuarios(usuarios);

    const usuarioActualizado = { ...usuarios[usuarioIndex] };
    delete usuarioActualizado.password;

    // Actualizar usuario actual si es el mismo
    if (this.currentUserValue?.idUsuario === idUsuario) {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(usuarioActualizado));
      this.currentUserSubject.next(usuarioActualizado);
    }

    return of(usuarioActualizado).pipe(delay(500));
  }

  cambiarPassword(idUsuario: number, passwordActual: string, nuevaPassword: string): Observable<{message: string}> {
    const usuarios = this.mockDataService.getUsuarios();
    const usuario = usuarios.find(u => u.idUsuario === idUsuario);

    if (!usuario) {
      return throwError(() => new Error('Usuario no encontrado'));
    }

    // En producción verificaríamos el hash
    usuario.password = nuevaPassword;
    this.mockDataService.saveUsuarios(usuarios);

    return of({
      message: 'Contraseña cambiada exitosamente'
    }).pipe(delay(500));
  }

  get currentUserValue(): Usuario | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return this.currentUserValue !== null;
  }

  hasRole(roles: string[]): boolean {
    const user = this.currentUserValue;
    return user ? roles.includes(user.rol) : false;
  }

  private getNextId(items: any[]): number {
    return items.length > 0 ? Math.max(...items.map(i => i.idUsuario || 0)) + 1 : 1;
  }

  private generateMockToken(): string {
    return 'mock-jwt-token-' + Math.random().toString(36).substring(7);
  }
}

