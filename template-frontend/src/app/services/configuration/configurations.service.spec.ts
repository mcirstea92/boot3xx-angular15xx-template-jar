import { TestBed } from '@angular/core/testing';

import { ConfigurationsService } from './configuration.service';

describe('ConfigurationService', () => {
  let service: ConfigurationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfigurationsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
