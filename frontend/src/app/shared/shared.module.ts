import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import {MatButtonModule} from '@angular/material/button'
import {MatGridListModule} from '@angular/material/grid-list'
import {MatFormFieldModule} from '@angular/material/form-field'
import {MatInputModule} from '@angular/material/input'
import {MatRadioModule} from '@angular/material/radio'
import {MatSelectModule} from '@angular/material/select'
import {MatDatepickerModule} from '@angular/material/datepicker'
import { MatNativeDateModule } from '@angular/material/core'
import { MatToolbarModule } from '@angular/material/toolbar'
import {MatDialogModule} from '@angular/material/dialog'
import {MatTableModule} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon'


const MaterialComponents = [MatButtonModule, MatGridListModule, MatFormFieldModule,MatInputModule, 
  MatRadioModule, MatDatepickerModule, MatNativeDateModule, MatToolbarModule,
  MatSelectModule, MatDialogModule, MatTableModule, MatIconModule];


@NgModule({
  declarations: [],
  imports: [
    MaterialComponents
  ],
  exports: [
    ReactiveFormsModule,
    FontAwesomeModule,
    FormsModule,
    MaterialComponents
  ]
})
export class SharedModule { }
