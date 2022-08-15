package com.etiya.northwind.business.responses.cartItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemListResponse {

    private int cartItemId;

    private int cartId;

    private int productId;

    private int quantity;

    private double unitPrice;

}
