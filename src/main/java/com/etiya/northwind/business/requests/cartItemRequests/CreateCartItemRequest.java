package com.etiya.northwind.business.requests.cartItemRequests;

import com.etiya.northwind.entities.concretes.Cart;
import com.etiya.northwind.entities.concretes.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItemRequest {
    @NotNull
    private int cartId;

    @NotNull
    private int productId;

    @NotNull
    private int quantity;

    @NotNull
    private double unitPrice;
}
