import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Product } from './product';
import { ProductPagin } from './productPagin';
import { PaginationParams } from './paginationParams';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  getProducts(): Observable<Product[]> {
      return this.http.get<Product[]>('/api/v1/products');
  }

  //getProductsPagin(params: any): Observable<ProductPagin> {
    //getProductsPagin(params: PaginationParams): Observable<ProductPagin> {
//       getProductsPagin(params: PaginationParams): Observable<ProductPagin> {
//     return this.http.get<ProductPagin>('/api/v1/products/pagination', { params });
// }
// }

getProductsPagin(paginationParams: PaginationParams): Observable<ProductPagin> {
  const params = new HttpParams()
  .set('page', paginationParams.page)
  .set('size', paginationParams.size)
  .set('sortDir', paginationParams.sortDir)
  .set('sort', paginationParams.sort);
  return this.http.get<ProductPagin>('/api/v1/products/pagination', { params });
}
}