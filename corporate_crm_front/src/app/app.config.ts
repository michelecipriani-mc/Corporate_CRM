import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';

// Per comunicare con il backend
import { provideHttpClient, withInterceptors } from '@angular/common/http';

// Importa l'intercettore
import { authTokenInterceptor } from './core/auth-token-interceptor'; 



export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authTokenInterceptor]))
  ]
};
