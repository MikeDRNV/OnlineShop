import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { CartItem } from './cartItem';
import { ProductQuantityDTO } from './product-quantity-dto';
import { ActiveCart } from './activeCart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) { }

  getProducts(): Observable<ActiveCart> {
    return this.http.get<ActiveCart>('/api/v1/cart/active/0/products');
  }

  toOrder(cartItems: CartItem[]) {
    return this.http.patch('/api/v1/cart/active/0/order', 0);
  }

  setQuantity(cartItem: CartItem) {
    const productQuantityDTO: ProductQuantityDTO = new ProductQuantityDTO(cartItem.productId, cartItem.quantity);
    return this.http.post<ProductQuantityDTO>('/api/v1/cart/active/0/changeProductQuantity', JSON.parse(JSON.stringify(productQuantityDTO)));
  }
}
