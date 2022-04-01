import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

import { ProductQuantityDTO } from './product-quantity-dto';

@Injectable({
  providedIn: 'root'
})
export class ProductCardService {

  constructor(private http: HttpClient) { }

  addProductToCart(productId: number) {
    const productQuantityDTO: ProductQuantityDTO = new ProductQuantityDTO(productId, 1);
    return this.http.post<ProductQuantityDTO>('/api/v1/cart/active/0/changeProductQuantity', JSON.parse(JSON.stringify(productQuantityDTO)));
  }
}
