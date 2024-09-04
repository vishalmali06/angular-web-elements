import { Injectable } from '@angular/core';

@Injectable()
export class CacheService {
  private cache: Map<String, any> = new Map();
  constructor() {}
  put(url: string, response: any) {
    console.log('chche miss');
    this.cache.set(url, response);
  }
  get(url: string) {
    console.log('chche hit');
    return this.cache.get(url);
  }
  clear(url: string) {
    this.cache.clear();
  }
}
