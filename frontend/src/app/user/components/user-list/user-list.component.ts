import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {
  MatTableDataSource,
  _MatTableDataSource,
} from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ModalComponent } from 'src/app/modal/modal.component';
import { UserService } from 'src/app/user/services/user.service';
import { User } from '../../entity/user';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users!: User[];
  response!: any;
  loading!: boolean;
  page!: number;
  page_number!: number;
  page_size: number = 10;
  totalElements: number = 0;
  totalPages!: number;
  sort: string = 'desc';
  sort_by: string = 'userId';
  dataSource!: _MatTableDataSource<any>;
  displayColumns: string[] = ['ID', 'Name', 'Actions'];

  constructor(
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService,
    private matDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.getUsers({ index: 1 });
  }

  getUsers(event: any) {
    // console.log(event);
    this.loading = true;
    this.page = event.index;
    this.page_number = this.page - 1;
    this.userService
      .getUsers(this.page_number, this.page_size, this.sort_by, this.sort)
      .subscribe(
        (resp) => {
          this.response = resp;
          if ((this.response.success = true)) {
            this.users = this.response.data.content;
            this.page_number = this.response.data.pageable.pageNumber;
            this.totalPages = this.response.data.totalPages;
            this.totalElements = this.response.data.totalElements;
            // console.log(this.users);
            this.dataSource = new MatTableDataSource(this.users);
          } else {
            this.toastr.error(this.response.messages, '', {
              timeOut: 2000,
              progressBar: false,
            });
          }
        },
        (err) => {
          console.log(err);
          this.toastr.error('Failed to Get User Details', '', {
            timeOut: 2000,
            progressBar: false,
          });
        }
      );
  }

  updateUser(userId: number) {
    this.router.navigate(['user/edit', userId]);
  }

  viewUser(userId: number) {
    this.router.navigate(['user/view', userId]);
  }

  deleteUser(userId: number) {
    const dialogRef = this.matDialog.open(ModalComponent, {
      width: '350px',
      data: {
        title: 'Delete User',
        body: 'Do you want to delete user ' + userId + '?',
        successText: 'Delete',
        cancelText: 'Cancel',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.userService.deleteUser(userId).subscribe(
          (resp) => {
            this.response = resp;
            console.log(this.response);
            if (this.response.success == true) {
              this.getUsers({ index: 1 });
              this.toastr.warning('User ' + userId + ' Deleted', '', {
                timeOut: 2000,
                progressBar: false,
              });
            }
          },
          (err) => console.log(err)
        );
      }
    });
  }

  paginationTo() {
    let current_page_limit = (this.page_number + 1) * this.page_size;
    if (current_page_limit < this.totalElements) {
      return current_page_limit;
    } else {
      return this.totalElements;
    }
  }

  sort_users_by(by: string) {
    this.sort = this.sort == 'desc' ? 'asc' : 'desc';

    if (by != this.sort_by) this.sort = 'asc';

    this.sort_by = by;
    this.getUsers({ index: this.page });
  }

  sort_users(by: string) {
    return {
      sorting_asc: this.sort == 'asc' && this.sort_by == by,
      sorting_desc: this.sort == 'desc' && this.sort_by == by,
      sorting: this.sort == 'asc' && this.sort_by == by,
    };
  }
}
