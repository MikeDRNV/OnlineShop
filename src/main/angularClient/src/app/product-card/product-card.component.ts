import { Component, OnInit, Input } from '@angular/core';

import { Product } from '../product';
import { ProductCardService } from '../product-card.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {

  @Input() product?: Product;

  done = false;

  constructor(private productCardService: ProductCardService) { }

  ngOnInit(): void {
  }

  public addProductToCart(productId: number): void {
    this.productCardService.addProductToCart(productId)
      .subscribe(
        (data : any) => {this.done = true;}
      );
  }

}
