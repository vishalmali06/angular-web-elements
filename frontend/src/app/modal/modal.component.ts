import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {

  title !: string;
  body !: string;
  successText !: string;
  cancelText !: string;

  constructor(public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) { 
    this.title = data.name;
    this.body = data.body;
    this.successText = data.successText;
    this.cancelText = data.cancelText;
  }

  ngOnInit(): void {
    //this.dialogRef.updateSize('30%', '20%');
  }



  onNoClick(): void {
    this.dialogRef.close(false);
  }

  onDelete(){
    this.dialogRef.close(true);
  }

}
