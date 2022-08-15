package com.etiya.northwind.api.controllers;

import com.etiya.northwind.business.abstracts.CartService;
import com.etiya.northwind.business.requests.cartRequests.CreateCartRequest;
import com.etiya.northwind.business.requests.cartRequests.DeleteCartRequest;
import com.etiya.northwind.business.responses.carts.CartListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.dataAccess.abstracts.CartRepository;
import com.etiya.northwind.entities.concretes.Cart;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;
    private final CartRepository cartRepository;

    public CartController(CartService cartService, CartRepository cartRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/getall")
    public DataResult<List<CartListResponse>> getAll(){
        return this.cartService.getAll();
    }

    @GetMapping("/get")
    public List<Cart> getCarts(){
        return cartRepository.findAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCartRequest createCartRequest){
        return this.cartService.add(createCartRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCartRequest deleteCartRequest){
        return this.cartService.delete(deleteCartRequest);
    }

    @GetMapping("/getbyid/{cartId}")
    public DataResult<CartListResponse> getById(@PathVariable int cartId){
        return this.cartService.getById(cartId);
    }
}
