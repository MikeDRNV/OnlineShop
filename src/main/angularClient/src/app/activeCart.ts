import { CartItem } from "./cartItem";

export interface ActiveCart {
    id: number;
    userId:number;
    cartItemDTOs: CartItem[];
}