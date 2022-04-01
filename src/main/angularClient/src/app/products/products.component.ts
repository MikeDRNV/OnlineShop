import { Component, OnInit, EventEmitter } from '@angular/core';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { PaginationParams } from '../paginationParams';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  products: Product[] = [];

  productDTOS: Product[] = [];

  page: number = 0;
  pageSize: number = 5;
  selectedPageSize: number = 5;
  count: number = 0;
  pageSizes = [3, 6, 9];
  sort: string = "";
  selectedPageSort: string = "";
  sorts = ["name", "price"];
  sortDir: string = "";
  selectedSortDir: string = "";
  sortsDir = ["Asc", "Desc"];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.retrieveProducts();
  }
  
  private getProduct(): void {
    this.productService.getProducts()
    .subscribe(products => this.products = products);
  }

  getRequestParams(pageParam: number, pageSize: number, sortDirParam: string, sortParam: string): PaginationParams {
    let params: PaginationParams = {page: pageParam, size: pageSize, sortDir: sortDirParam, sort: sortParam};
    if (pageParam) {
      params.page = pageParam - 1;
    }
    if (pageSize) {
      params.size = pageSize;
    }
    if (sortDirParam) {
      params.sortDir = sortDirParam;
    }
    if (sortParam) {
      params.sort = sortParam;
    }
    return params;
  }

  retrieveProducts(): void {
    const params = this.getRequestParams(this.page, this.pageSize, this.sortDir, this.sort);
    this.productService.getProductsPagin(params)
    .subscribe(
      response => {
        const { productDTOS, totalItems } = response;
        this.productDTOS = productDTOS;
        this.count = totalItems;
      },
      error => {
        console.log(error);
      });
  }

  handlePageChange(event: number): void {
    console.log(event);
    this.page = event;
    this.retrieveProducts();
  }

  setPageSize(): void {
    this.pageSize = this.selectedPageSize;
    this.page = 1;
    this.retrieveProducts();
  }

  setPageSort(): void {
    this.sort = this.selectedPageSort;
    this.retrieveProducts();
  }

  setPageSortDir(): void {
    this.sortDir = this.selectedSortDir;
    this.retrieveProducts();
  }
}
