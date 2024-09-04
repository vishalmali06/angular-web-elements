import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse,
  HttpEventType,
} from '@angular/common/http';
import { Observable, tap, of } from 'rxjs';
import { CacheService } from 'src/app/user/services/cache.service';

@Injectable()
export class CacheInterceptorInterceptor implements HttpInterceptor {
  constructor(private cacheService: CacheService) {}
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (request.method !== 'GET') {
      return next.handle(request);
    }
    const cacheResponse = this.cacheService.get(request.urlWithParams);
    if (cacheResponse) {
      return of(cacheResponse);
    }
    return next.handle(request).pipe(
      tap((event: HttpEvent<any>) => {
        if (event.type === HttpEventType.Response) {
          this.cacheService.put(request.urlWithParams, event);
        }
      })
    );
  }
}
