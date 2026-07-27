import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../auth/auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = localStorage.getItem('token');
  const role = authService.getRoleFromToken();
  const companyId = authService.getCompanyIdFromToken();

  const country = authService.getCountryFromToken();
  const currencyCode = authService.getCurrencyFromToken();

  let authReq = req;

  if (token) {
    // Standardized Authorization Headers
    const headersConfig: { [header: string]: string } = {
      Authorization: `Bearer ${token}`,
      'X-User-Role': role || '',
      'X-User-Country': country,
      'X-User-Currency': currencyCode
    };

    // ONLY append companyId query parameters to data-fetching GET requests
    if (req.method === 'GET') {
      const finalCompanyId = (role === 'ROLE_SUPPLIER' && companyId) ? companyId.toString() : '0';
      authReq = req.clone({
        setHeaders: headersConfig,
        setParams: { companyId: finalCompanyId }
      });
    } else {
      // POST, PUT, DELETE requests keep their bodies clean without URL param distortion
      authReq = req.clone({
        setHeaders: headersConfig
      });
    }
  }

  return next(authReq).pipe(
    catchError((error) => {
      if (error.status === 401) {
        authService.logout(); 
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};