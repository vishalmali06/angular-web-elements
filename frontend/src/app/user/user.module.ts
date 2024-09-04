import { NgModule } from '@angular/core';
import { AddUserComponent } from './components/user/user.component';
import { UserRoutingModule } from './user-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CommonModule } from '@angular/common';
import { UserListComponent } from './components/user-list/user-list.component';
import { PaginationComponent } from './components/pagination/pagination.component';



@NgModule({
  declarations: [
    UserListComponent,
    PaginationComponent,
    AddUserComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    SharedModule
  ],
  // exports:[
  //   UserListComponent,
  //   PaginationComponent,
  //   AddUserComponent,
  //   ReactiveFormsModule,
  //   MaterialModule,
  //   FontAwesomeModule,
  // ]
})
export class UserModule { }
