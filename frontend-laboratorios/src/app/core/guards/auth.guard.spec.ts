import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { authGuard, roleGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

describe('Auth Guards', () => {
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;
  let mockRoute: ActivatedRouteSnapshot;
  let mockState: RouterStateSnapshot;

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated', 'hasRole']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    mockRoute = {} as ActivatedRouteSnapshot;
    mockState = { url: '/dashboard' } as RouterStateSnapshot;
  });

  describe('authGuard', () => {
    it('should allow access when user is authenticated', () => {
      authService.isAuthenticated.and.returnValue(true);

      const result = TestBed.runInInjectionContext(() => 
        authGuard(mockRoute, mockState)
      );

      expect(result).toBeTrue();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('should redirect to login when user is not authenticated', () => {
      authService.isAuthenticated.and.returnValue(false);

      const result = TestBed.runInInjectionContext(() => 
        authGuard(mockRoute, mockState)
      );

      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/login'], { queryParams: { returnUrl: '/dashboard' } });
    });
  });

  describe('roleGuard', () => {
    it('should allow access when user has required role', () => {
      mockRoute.data = { roles: ['ADMINISTRADOR'] };
      authService.isAuthenticated.and.returnValue(true);
      authService.hasRole.and.returnValue(true);

      const result = TestBed.runInInjectionContext(() => 
        roleGuard(mockRoute, mockState)
      );

      expect(result).toBeTrue();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('should redirect to dashboard when user does not have required role', () => {
      mockRoute.data = { roles: ['ADMINISTRADOR'] };
      authService.isAuthenticated.and.returnValue(true);
      authService.hasRole.and.returnValue(false);

      const result = TestBed.runInInjectionContext(() => 
        roleGuard(mockRoute, mockState)
      );

      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
    });

    it('should redirect to login when user is not authenticated', () => {
      mockRoute.data = { roles: ['ADMINISTRADOR'] };
      authService.isAuthenticated.and.returnValue(false);

      const result = TestBed.runInInjectionContext(() => 
        roleGuard(mockRoute, mockState)
      );

      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
  });
});


