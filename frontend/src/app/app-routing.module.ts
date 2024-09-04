import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { CustomPreloadingStrategyService } from './user/services/custom-preloading-strategy.service';


const routes: Routes = [
  
  {
    path:'user', 
    data:{preload:true},
    loadChildren: () => import('./user/user.module').then(m => m.UserModule)
  },
  {path: '', redirectTo: 'user', pathMatch:'full'},
  {path: '**', component: PageNotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {preloadingStrategy:CustomPreloadingStrategyService})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
