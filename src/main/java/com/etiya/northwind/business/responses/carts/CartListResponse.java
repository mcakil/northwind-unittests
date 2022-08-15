package com.etiya.northwind.business.responses.carts;

import com.etiya.northwind.business.responses.cartItems.CartItemListResponse;
import com.etiya.northwind.entities.concretes.CartItem;
import com.etiya.northwind.entities.concretes.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartListResponse {

    private int cartId;

    private String customerId;

    //private List<Integer> cartItemsIds;

}
