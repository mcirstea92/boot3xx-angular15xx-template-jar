import { TestBed } from '@angular/core/testing';

import { ServiceContainer } from './service-container';

describe('ServiceContainer', () => {
  let service: ServiceContainer;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceContainer);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
