import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { MockDataService } from './mock-data.service';
import { LoginRequest, LoginResponse, RegistroRequest, Usuario } from '../../shared/models/usuario.model';
import { ApiResponse } from '../../shared/models/api-response.model';
import { environment } from '../../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let mockDataService: jasmine.SpyObj<MockDataService>;

  beforeEach(() => {
    const mockDataSpy = jasmine.createSpyObj('MockDataService', ['getUsuarios']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: MockDataService, useValue: mockDataSpy }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    mockDataService = TestBed.inject(MockDataService) as jasmine.SpyObj<MockDataService>;
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login', () => {
    it('should login successfully and store user data', (done) => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password123'
      };

      const mockLoginResponse: LoginResponse = {
        idUsuario: 1,
        nombre: 'Test',
        apellido: 'User',
        email: 'test@example.com',
        rol: 'PACIENTE',
        token: 'mock-jwt-token',
        mensaje: 'Login exitoso'
      };

      const mockApiResponse: ApiResponse<LoginResponse> = {
        traceId: 'test-trace-id',
        code: 'SUCCESS',
        message: 'Login exitoso',
        data: mockLoginResponse
      };

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockLoginResponse);
        expect(service.currentUserValue).toBeTruthy();
        expect(service.currentUserValue?.email).toBe('test@example.com');
        expect(service.currentUserValue?.token).toBe('mock-jwt-token');
        done();
      });

      const req = httpMock.expectOne(`${environment.apiUrl.usuarios}/usuarios/login`);
      expect(req.request.method).toBe('POST');
      req.flush(mockApiResponse);
    });

    it('should handle login error', (done) => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'wrongpassword'
      };

      service.login(loginRequest).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error).toBeTruthy();
          done();
        }
      });

      const req = httpMock.expectOne(`${environment.apiUrl.usuarios}/usuarios/login`);
      req.flush({ message: 'Credenciales incorrectas' }, { status: 401, statusText: 'Unauthorized' });
    });
  });

  describe('register', () => {
    it('should register user successfully', (done) => {
      const registroRequest: RegistroRequest = {
        nombre: 'New',
        apellido: 'User',
        email: 'new@example.com',
        password: 'Password123*',
        rol: 'PACIENTE'
      };

      const mockUsuario: Usuario = {
        idUsuario: 1,
        nombre: 'New',
        apellido: 'User',
        email: 'new@example.com',
        rol: 'PACIENTE',
        activo: true
      };

      const mockApiResponse: ApiResponse<Usuario> = {
        traceId: 'test-trace-id',
        code: 'SUCCESS',
        message: 'Usuario registrado',
        data: mockUsuario
      };

      service.register(registroRequest).subscribe(usuario => {
        expect(usuario).toEqual(mockUsuario);
        done();
      });

      const req = httpMock.expectOne(`${environment.apiUrl.usuarios}/usuarios/registro`);
      expect(req.request.method).toBe('POST');
      req.flush(mockApiResponse);
    });
  });

  describe('logout', () => {
    it('should clear user data on logout', () => {
      // Simular usuario logueado
      const mockUser: Usuario = {
        idUsuario: 1,
        nombre: 'Test',
        apellido: 'User',
        email: 'test@example.com',
        rol: 'PACIENTE',
        token: 'mock-token',
        activo: true
      };
      localStorage.setItem('currentUser', JSON.stringify(mockUser));

      service.logout();

      expect(localStorage.getItem('currentUser')).toBeNull();
      expect(service.currentUserValue).toBeNull();
    });
  });

  describe('isAuthenticated', () => {
    it('should return true when user is logged in', () => {
      const mockUser: Usuario = {
        idUsuario: 1,
        nombre: 'Test',
        apellido: 'User',
        email: 'test@example.com',
        rol: 'PACIENTE',
        token: 'mock-token',
        activo: true
      };
      localStorage.setItem('currentUser', JSON.stringify(mockUser));
      
      // Recrear servicio para que lea del localStorage
      service = TestBed.inject(AuthService);

      expect(service.isAuthenticated()).toBeTrue();
    });

    it('should return false when user is not logged in', () => {
      expect(service.isAuthenticated()).toBeFalse();
    });
  });

  describe('hasRole', () => {
    it('should return true when user has required role', () => {
      const mockUser: Usuario = {
        idUsuario: 1,
        nombre: 'Admin',
        apellido: 'User',
        email: 'admin@example.com',
        rol: 'ADMINISTRADOR',
        token: 'mock-token',
        activo: true
      };
      localStorage.setItem('currentUser', JSON.stringify(mockUser));
      service = TestBed.inject(AuthService);

      expect(service.hasRole(['ADMINISTRADOR'])).toBeTrue();
    });

    it('should return false when user does not have required role', () => {
      const mockUser: Usuario = {
        idUsuario: 1,
        nombre: 'Test',
        apellido: 'User',
        email: 'test@example.com',
        rol: 'PACIENTE',
        token: 'mock-token',
        activo: true
      };
      localStorage.setItem('currentUser', JSON.stringify(mockUser));
      service = TestBed.inject(AuthService);

      expect(service.hasRole(['ADMINISTRADOR'])).toBeFalse();
    });
  });
});


