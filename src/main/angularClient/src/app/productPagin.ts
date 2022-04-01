import { Product } from "./product";

export interface ProductPagin {
    totalItems: number;
    productDTOS: Product[];
    totalPages: number;
    currentPage: number;
}