import { Component, OnInit, Input } from '@angular/core';
import { Output, EventEmitter } from '@angular/core';
import { CartItem } from '../cartItem';

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css']
})
export class CartItemComponent implements OnInit {

  @Input() cartItem?: CartItem;

  @Output() changeQuantityEvent = new EventEmitter<number>();

  @Output() decreaseQuantityEvent = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  increaseQuantity(productId: number) {
    this.changeQuantityEvent.emit(productId);
  }

  decreaseQuantity(productId: number) {
    if (this.cartItem!.quantity > 0) {
      this.decreaseQuantityEvent.emit(productId);
    }
  }
}
