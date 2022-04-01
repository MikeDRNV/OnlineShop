import { Component, OnInit } from '@angular/core';

import { CartService } from '../cart.service';
import { CartItem } from '../cartItem';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cartItems: CartItem[] = [];

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  private getProducts() {
    this.cartService.getProducts().subscribe(activeCart => this.cartItems = activeCart.cartItemDTOs);
  }

toOrder(cartItems: CartItem[]) {
  cartItems.forEach((cartItem) => {
    this.cartService.setQuantity(cartItem).subscribe();
  })
  this.cartService.toOrder(cartItems).subscribe();
}

  increaseQuantity(productId: number) {
    this.cartItems.forEach(item => (item.productId === productId) ? ++item.quantity : item.quantity);
  }

  decreaseQuantity(productId: number) {
    this.cartItems.forEach(item => (item.productId === productId) ? --item.quantity : item.quantity);
  }
}
