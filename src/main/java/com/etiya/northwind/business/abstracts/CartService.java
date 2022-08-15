package com.etiya.northwind.business.abstracts;

import com.etiya.northwind.business.requests.cartRequests.CreateCartRequest;
import com.etiya.northwind.business.requests.cartRequests.DeleteCartRequest;
import com.etiya.northwind.business.responses.carts.CartListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.entities.concretes.Cart;

import java.util.List;

public interface CartService {
    Result add(CreateCartRequest createCartRequest);
    Result delete(DeleteCartRequest deleteCartRequest);

    Cart getCart(String customerId);

    DataResult<CartListResponse> getById(int cartId);

    DataResult<List<CartListResponse>> getAll();
}
