import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ModalComponent } from './modal/modal.component';
import { LazyElementsModule } from '@angular-extensions/elements';
import { CacheInterceptorInterceptor } from 'src/core/cache-interceptor.interceptor';
import { CacheService } from './user/services/cache.service';
import { UserService } from './user/services/user.service';

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    ModalComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ToastrModule.forRoot(),
    LazyElementsModule
  ],
  providers: [
    UserService,
    CacheService,
    {
      provide : HTTP_INTERCEPTORS,
      useClass : CacheInterceptorInterceptor,
      multi : true,
    }
  ],
  bootstrap: [AppComponent],
  schemas : [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
})
export class AppModule { }
