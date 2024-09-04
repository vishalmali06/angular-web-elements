import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../entity/user';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseURL = 'http://localhost:6789/api/user';
  constructor(private httpClient : HttpClient) { }


  getUsers(offset:number, pageSize:number, sortBy:string, sort:string): Observable<Object>{
    let params = new HttpParams()
    .set('offset', offset)
    .set('pageSize', pageSize)
    .set('sortBy', sortBy)
    .set('sort', sort);

    // Cache  on Service leavel 
    // const cacheData = this.cacheService.get('users');
    // if (cacheData) {
    //   return of(cacheData);
    // }
    // return this.httpClient.get(`${this.baseURL}/pageable`, { params: params }).pipe(
    //   tap((data: any) => {
    //     this.cacheService.put('users', data);
    //   })
    // );

    return this.httpClient.get(`${this.baseURL}/pageable`,{ params: params });
  }

  createUser(user: User): Observable<Object>{
    return this.httpClient.post(`${this.baseURL}`, user);
  }

  editUser(user: User, jsonData: any): Observable<Object>{
    return this.httpClient.put(`${this.baseURL}`, {user, jsonData});
  }

  getUser(userId: number): Observable<Object>{
    return this.httpClient.get(`${this.baseURL}/${userId}`);
  }

  deleteUser(userId: number): Observable<Object>{
    return this.httpClient.delete(`${this.baseURL}/${userId}`);
  }

}
