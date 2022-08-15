package com.etiya.northwind.business.concretes;

import com.etiya.northwind.business.abstracts.CartService;
import com.etiya.northwind.business.abstracts.CustomerService;
import com.etiya.northwind.business.requests.cartRequests.CreateCartRequest;
import com.etiya.northwind.business.requests.cartRequests.DeleteCartRequest;
import com.etiya.northwind.business.responses.carts.CartListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.core.results.SuccessResult;
import com.etiya.northwind.dataAccess.abstracts.CartRepository;
import com.etiya.northwind.dataAccess.abstracts.CustomerRepository;
import com.etiya.northwind.entities.concretes.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartManager implements CartService {

    private final CartRepository cartRepository;
    private final ModelMapperService modelMapperService;
    private final CustomerService customerService;

    @Autowired
    public CartManager(CartRepository cartRepository, ModelMapperService modelMapperService, CustomerService customerService){
        this.cartRepository = cartRepository;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }
    @Override
    public Cart getCart(String customerId){
        return cartRepository.getByCustomerId(customerId).get();
    }

    @Override
    public Result add(CreateCartRequest createCartRequest) {
        checkCartExists(createCartRequest.getCustomerId());
        Cart cart = new Cart(); //this.modelMapperService.forRequest().map(createCartRequest, Cart.class);
        cart.setCustomer(customerService.findById(createCartRequest.getCustomerId()));
        cartRepository.save(cart);
        return new SuccessResult("Sepet eklendi.");
    }

    @Override
    public Result delete(DeleteCartRequest deleteCartRequest) {
        cartRepository.deleteById(deleteCartRequest.getCartId());
        return new SuccessResult("Sepet silindi.");
    }

    @Override
    public DataResult<List<CartListResponse>> getAll() {
        List<Cart> carts = this.cartRepository.findAll();
        List<CartListResponse> response = carts.stream().map(cart -> this.modelMapperService.forResponse().map(cart, CartListResponse.class)).collect(Collectors.toList());
        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<CartListResponse> getById(int cartId) {
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new BusinessException("Verilen id ile kart bulunamadı"));
        CartListResponse response = this.modelMapperService.forResponse().map(cart, CartListResponse.class);
        return new SuccessDataResult<>(response);
    }

    private void checkCartExists(String customerId) {
        if (cartRepository.getByCustomerId(customerId).isPresent()) {
            throw new BusinessException("Müşterinin sepeti zaten var.");
        }
    }

}
