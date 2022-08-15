package com.etiya.northwind.api.controllers;

import com.etiya.northwind.business.abstracts.CartItemService;
import com.etiya.northwind.business.requests.cartItemRequests.CreateCartItemRequest;
import com.etiya.northwind.business.requests.cartItemRequests.DeleteCartItemRequest;
import com.etiya.northwind.business.requests.cartItemRequests.UpdateCartItemRequest;
import com.etiya.northwind.business.responses.cartItems.CartItemListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cartItems")
public class CartItemsController {
    private final CartItemService cartItemService;

    public CartItemsController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/getall")
    public DataResult<List<CartItemListResponse>> getAll(){
        return this.cartItemService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCartItemRequest createCartItemRequest){
        return this.cartItemService.add(createCartItemRequest);
    }

    @PostMapping("/update/{cartItemId}")
    public Result update(@PathVariable @Valid int cartItemId, @RequestBody @Valid UpdateCartItemRequest updateCartItemRequest){
        return this.cartItemService.update(cartItemId, updateCartItemRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCartItemRequest deleteCartItemRequest){
        return this.cartItemService.delete(deleteCartItemRequest);
    }

    @GetMapping("/getbyid/{cartId}")
    public DataResult<CartItemListResponse> getById(@PathVariable int cartId){
        return this.cartItemService.getById(cartId);
    }
}
