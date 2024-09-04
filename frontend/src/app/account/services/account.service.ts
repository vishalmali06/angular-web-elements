import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Account } from '../entity/account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private baseURL = 'http://localhost:6789/api/account';
  constructor(private httpClient : HttpClient) { }

  getAccounts(): Observable<Object>{
    return this.httpClient.get(`${this.baseURL}`);
  }

  createAccount(account: Account): Observable<Object>{
    return this.httpClient.post(`${this.baseURL}`, account);
  }

  getAccount(accountId: number): Observable<Object>{
    return this.httpClient.get(`${this.baseURL}/${accountId}`);
  }

  getAccountByUser(userId: number): Observable<Object>{
    return this.httpClient.get(`${this.baseURL}/user/${userId}`);
  }
}
