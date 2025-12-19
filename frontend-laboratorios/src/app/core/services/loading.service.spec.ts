import { TestBed } from '@angular/core/testing';
import { LoadingService } from './loading.service';

describe('LoadingService', () => {
  let service: LoadingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with loading false', (done) => {
    service.loading$.subscribe(loading => {
      expect(loading).toBeFalse();
      done();
    });
  });

  it('should set loading to true when show is called', (done) => {
    service.show();
    
    service.loading$.subscribe(loading => {
      expect(loading).toBeTrue();
      expect(service.isLoading).toBeTrue();
      done();
    });
  });

  it('should set loading to false when hide is called', (done) => {
    service.show();
    service.hide();
    
    service.loading$.subscribe(loading => {
      expect(loading).toBeFalse();
      expect(service.isLoading).toBeFalse();
      done();
    });
  });

  it('should handle multiple concurrent requests', (done) => {
    service.show(); // Request 1
    service.show(); // Request 2
    service.show(); // Request 3

    expect(service.isLoading).toBeTrue();

    service.hide(); // Request 1 completes
    expect(service.isLoading).toBeTrue(); // Still loading (2 requests pending)

    service.hide(); // Request 2 completes
    expect(service.isLoading).toBeTrue(); // Still loading (1 request pending)

    service.hide(); // Request 3 completes
    
    service.loading$.subscribe(loading => {
      expect(loading).toBeFalse();
      expect(service.isLoading).toBeFalse();
      done();
    });
  });

  it('should not go below zero request count', () => {
    service.hide();
    service.hide();
    service.hide();

    expect(service.isLoading).toBeFalse();
  });
});


