import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddUserComponent } from './components/user/user.component';
import { UserListComponent } from './components/user-list/user-list.component';

const routes: Routes = [
    {path: 'list', component: UserListComponent},
    {path: 'add', component: AddUserComponent},
    {path: 'view/:userId', component: AddUserComponent},
    {path: 'edit/:userId', component: AddUserComponent},
    {path: '', redirectTo: 'list', pathMatch:'full'},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
