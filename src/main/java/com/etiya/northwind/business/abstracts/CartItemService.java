package com.etiya.northwind.business.abstracts;

import com.etiya.northwind.business.requests.cartItemRequests.CreateCartItemRequest;
import com.etiya.northwind.business.requests.cartItemRequests.DeleteCartItemRequest;
import com.etiya.northwind.business.requests.cartItemRequests.UpdateCartItemRequest;
import com.etiya.northwind.business.responses.cartItems.CartItemListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.entities.concretes.CartItem;

import java.util.Collection;
import java.util.List;

public interface CartItemService {

    void deleteCartItemsByCartId(int cartId);
    void deleteById(int cartItemId);

    Result add(CreateCartItemRequest createCartItemRequest);

    Result update(int cartItemId, UpdateCartItemRequest updateCartItemRequest);

    Result delete(DeleteCartItemRequest deleteCartItemRequest);

    DataResult<List<CartItemListResponse>> getAll();

    DataResult<CartItemListResponse> getById(int cartId);

}
