import { TestBed } from '@angular/core/testing';
import { HttpInterceptorFn, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';
import { Usuario } from '../../shared/models/usuario.model';
import { of } from 'rxjs';

describe('authInterceptor', () => {
  let authService: jasmine.SpyObj<AuthService>;

  const executeInterceptor: HttpInterceptorFn = (req, next) => 
    TestBed.runInInjectionContext(() => authInterceptor(req, next));

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', [], {
      currentUserValue: null
    });

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  it('should add Authorization header when user is authenticated', (done) => {
    const mockUser: Usuario = {
      idUsuario: 1,
      nombre: 'Test',
      apellido: 'User',
      email: 'test@example.com',
      rol: 'PACIENTE',
      token: 'mock-jwt-token',
      activo: true
    };

    Object.defineProperty(authService, 'currentUserValue', {
      get: () => mockUser
    });

    const mockRequest = new HttpRequest('GET', '/api/test');
    const mockNext: HttpHandlerFn = (req) => {
      expect(req.headers.has('Authorization')).toBeTrue();
      expect(req.headers.get('Authorization')).toBe('Bearer mock-jwt-token');
      done();
      return of({} as any);
    };

    executeInterceptor(mockRequest, mockNext).subscribe();
  });

  it('should not add Authorization header when user is not authenticated', (done) => {
    Object.defineProperty(authService, 'currentUserValue', {
      get: () => null
    });

    const mockRequest = new HttpRequest('GET', '/api/test');
    const mockNext: HttpHandlerFn = (req) => {
      expect(req.headers.has('Authorization')).toBeFalse();
      done();
      return of({} as any);
    };

    executeInterceptor(mockRequest, mockNext).subscribe();
  });
});


