import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormArray } from '@angular/forms';

import { UserService } from 'src/app/user/services/user.service';

import {faTrashAlt} from '@fortawesome/free-solid-svg-icons'

import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../entity/user';
import { Account } from 'src/app/account/entity/account';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class AddUserComponent implements OnInit {

  loading : boolean = false;
  response!: any;
  maxDate: Date = new Date();
  user!: FormGroup;
  u : User = new User();
  accounts !: FormArray;
  pageTitle !: string;
  isView : boolean = false;
  isEdit : boolean = false;
  purgedAccounts: any[] = [];

  accountsList !: Account[];

  validationMessages:any={
    'name':{
      'required':"Name Required",
      'pattern':"Name Should Not Contain Special Character"
    },
    'emailId':{
      'required':"Email ID Required",
      'pattern':"Invalid Email ID"
    },
    'mobileNo':{
      'required':"Mobile Number Required",
      'pattern':"Mobile no should be 10 digit long"
    },
    'secondaryMobileNo':{
      'required':"Secondary Mobile Number Required",
      'pattern':"Secondary Mobile no should be 10 digit long or empty"
    },
    'gender':{
      'required':"gender Required"
    },
    'dob':{
      'required':"Date of birth Required",
      'pattern':"DOB should be in mm/dd/yyyy format"
    }
  };

  formErrors:any={
  };

  genderOptions: any[] =   [
    {"name": "Male", ID: "g1", "checked": true, value:'M'},
    {"name": "Female", ID: "g2", "checked": false, value:'F'}
  ]

  AccountTypeOptions: any[] =   [
    {"name": "Current", ID: "t1", value:'C'},
    {"name": "Saving", ID: "22", value:'S'}
  ]

  faTrashAlt = faTrashAlt;

  constructor(private fb: FormBuilder, private service:UserService, 
    private route: ActivatedRoute, private router: Router,
    private toastr : ToastrService) {
    
   }

  ngOnInit(){
    console.log(this.router.url);
    if(this.router.url.startsWith('/user/view')){
      this.isView = true;
    }

    this.addAccount();
    this.user = this.fb.group({
      userId: [''],
      name: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9]{0,40}$')]],
      emailId: ['', [Validators.required, Validators.pattern('^[a-zA-Z_]+\@[a-zA-Z_]+\\.[a-zA-Z]{2,3}$')]],
      mobileNo: ['', [Validators.required, Validators.pattern('[0-9]{10}')]],
      secondaryMobileNo: ['', [Validators.pattern('^(\\s*|\\d{10})$')]],
      dob: ['', [Validators.required]],
      gender: ['M', [Validators.required]],
      accounts: this.accounts
    });

    const uId = this.route.snapshot.params['userId']; 
    if(uId){
      if(!this.isView){
        this.isEdit = true;
        this.pageTitle = "Edit User"
      }else{
        this.pageTitle = "View User"
      }
      this.getUser(+uId);
    }
    else{
      this.pageTitle = "Create User"
    }
    
    this.user.valueChanges.subscribe(v => {
      this.logValidatinErrors(this.user);  
    });

  }

  getUser(uId:number){
    this.service.getUser(uId).subscribe(resp => {
      this.response = resp;
      console.log(this.response);
      
      if(this.response.success == true){
        this.editUser(this.response.data);
      }
      else{
        this.toastr.error(this.response.messages, "", {
          timeOut : 2000,
          progressBar: false
        });
      }
    },
    err =>{ 
      console.log(err)
      this.toastr.error("Failed to Update User Details", "", {
        timeOut : 2000,
        progressBar: false
      });
    });
  }

  editUser(u:User){
    this.user.patchValue({
      userId: u.userId,
      name:u.name,
      emailId: u.emailId,
      mobileNo: u.mobileNo,
      secondaryMobileNo: u.secondaryMobileNo=="0"?"":u.secondaryMobileNo,
      gender: u.gender,
      dob:u.dob
    });
    this.accountsList = u.accounts;
    this.user.setControl('accounts', this.setExistingAccounts(u.accounts));
    if(this.isView){
      this.user.disable();
    }
  }

  setExistingAccounts(account:Account[]){
    console.log(account);
    
    this.accounts.clear();
    account.forEach(a => {
      this.accounts.push(this.fb.group({
        accountId:a.accountId,
        branchName:a.branchName,
        accountType:a.accountType,
        accountBalance:a.accountBalance
      }))
    }); 

    console.log(this.accounts);
    
    return this.accounts;
  }

  addAccountButtonClicked(){
    this.accounts.push(this.getAccount())
  }

  addAccount(){
    this.accounts = this.fb.array([
      this.getAccount()
      ])
  }

  getAccount(){
    return this.fb.group({
      accountId: [''],
      branchName: ['', [Validators.required, Validators.pattern('[0-9a-zA-Z]{3,40}')]],
      accountType:['', [Validators.required]],
      accountBalance:['', [Validators.required, Validators.pattern('^[0-9]{0,9}.[0-9]{0,2}$')]]
    })
  }

  cancel(): void{
    this.router.navigate(['user/list']);
  }

  delAccount(index:number){
    let ac : any = this.accounts.at(index);

    if(ac.get('accountId') !== ""){
      let a : Account = new Account();
        a.accountId = ac.get('accountId')?.value;
        a.branchName = ac.get('branchName')?.value;
        a.accountType = ac.get('accountType')?.value;
        a.accountBalance = ac.get('accountBalance')?.value;
        this.purgedAccounts.push(a);
    }
    
    this.accounts.removeAt(index);
  }
 
  onSubmit(): void{
    console.log(this.getJsonData())
    if(this.user.status === 'VALID'){
      this.u.userId= this.user.get('userId')?.value;
      this.u.name = this.user.get('name')?.value;
      this.u.emailId = this.user.get('emailId')?.value;
      this.u.mobileNo = this.user.get('mobileNo')?.value;
      this.u.secondaryMobileNo = this.user.get('secondaryMobileNo')?.value == ""? "0" :this.user.get('secondaryMobileNo')?.value;
      this.u.dob = this.user.get('dob')?.value;
      this.u.gender = this.user.get('gender')?.value;

      let ac : Account[] = [];
      this.accounts.controls.forEach(account => {
        let a : Account = new Account();
        a.accountId = account.get('accountId')?.value;
        a.branchName = account.get('branchName')?.value;
        a.accountType = account.get('accountType')?.value;
        a.accountBalance = account.get('accountBalance')?.value;
        ac.push(a);
      })

      this.u.accounts = ac;
      
      if(this.u.userId){
        this.service.editUser(this.u, this.getJsonData()).subscribe(resp => {
          this.response = resp;
          if(this.response.success == true){
              console.log("updated");
              this.toastr.success("User "+this.u.userId+" Updated", "", {
                timeOut : 2000,
                progressBar: false
              });
              this.router.navigate(['user/list']);
          }
          else{
            this.toastr.error(this.response.messages, "", {
              timeOut : 2000,
              progressBar: false
            });
          }
        },
        err =>{ 
          console.log(err)
          this.toastr.error("Failed to Update User Details", "", {
            timeOut : 2000,
            progressBar: false
          });
        }
        );
      }
      else{
          this.service.createUser(this.u).subscribe(resp => {
            this.response = resp;
            if(this.response.success == true){
                console.log("saved");
                this.toastr.success("User "+this.response.data.userId+" Created", "", {
                  timeOut : 2000,
                  progressBar: false
                });
                this.router.navigate(['user/list']);
            }
            else{
              this.toastr.error(this.response.messages, "", {
                timeOut : 2000,
                progressBar: false
              });
            }
          },
          err =>{ 
            console.log(err)
            this.toastr.error("Failed to Save User Details", "", {
              timeOut : 2000,
              progressBar: false
            });
          }
          );
        }
      }
    
  }

 getJsonData(){
  let createdAccounts: any[] = [];
  let modifiedAccounts: any[] = [];

  this.accounts.controls.forEach(account => {
    let a : Account = new Account();
    a.accountId = account.get('accountId')?.value;
    a.branchName = account.get('branchName')?.value;
    a.accountType = account.get('accountType')?.value;
    a.accountBalance = account.get('accountBalance')?.value;
    if(a.accountId)
      modifiedAccounts.push(a);
    else
      createdAccounts.push(a);
  });
  return{
    created:createdAccounts,
    modified:modifiedAccounts,
    purged:this.purgedAccounts
  }
 }

 logValidatinErrors(fgroup:FormGroup = this.user){
    Object.keys(fgroup.controls).forEach((key:string) =>{
      const abstractControl= fgroup.get(key);
      if(abstractControl instanceof FormGroup){
        this.logValidatinErrors(abstractControl);
      }
      else{
        this.formErrors[key] = '';
        if(abstractControl && !abstractControl.valid && (abstractControl.touched || abstractControl.dirty)){
          const messages= this.validationMessages[key];
          console.log(messages);
          for(const errorKey in abstractControl.errors){
            if(errorKey){
              this.formErrors[key] += messages[errorKey] + ' ';
            }
          }
        }
      }

    });
 }
}
