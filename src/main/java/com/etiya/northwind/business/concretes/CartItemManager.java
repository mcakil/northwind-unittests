package com.etiya.northwind.business.concretes;

import com.etiya.northwind.business.abstracts.CartItemService;
import com.etiya.northwind.business.requests.cartItemRequests.CreateCartItemRequest;
import com.etiya.northwind.business.requests.cartItemRequests.DeleteCartItemRequest;
import com.etiya.northwind.business.requests.cartItemRequests.UpdateCartItemRequest;
import com.etiya.northwind.business.responses.cartItems.CartItemListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.core.results.SuccessResult;
import com.etiya.northwind.dataAccess.abstracts.CartItemsRepository;
import com.etiya.northwind.entities.concretes.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemManager implements CartItemService {

    private final CartItemsRepository cartItemsRepository;
    private final ModelMapperService modelMapperService;

    public CartItemManager(CartItemsRepository cartItemsRepository, ModelMapperService modelMapperService) {
        this.cartItemsRepository = cartItemsRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public void deleteCartItemsByCartId(int cartId) {
        List<Integer> cartItemIds = cartItemsRepository.getCartItemIdByCartId(cartId);
        cartItemIds.stream().forEach(cartItemsRepository::deleteById);
    }

    @Override
    public void deleteById(int cartItemId) {
        cartItemsRepository.deleteById(cartItemId);
    }

    @Override
    public Result add(CreateCartItemRequest createCartItemRequest) {
        CartItem cartItem = this.modelMapperService.forRequest().map(createCartItemRequest, CartItem.class);
        cartItem.setCartItemId(0);
        System.out.println(cartItem.getCart().getCartId());
        cartItemsRepository.save(cartItem);
        return new SuccessResult("Ürün sepete başarıyla eklendi.");
    }

    @Override
    public Result update(int cartItemId, UpdateCartItemRequest updateCartItemRequest) {
        CartItem cartItem = this.cartItemsRepository.findById(cartItemId).orElseThrow(() -> {
            throw new BusinessException("Güncellenecek ürün sepette bulunamadı.");
        });
        cartItem.setQuantity(updateCartItemRequest.getQuantity());
        cartItemsRepository.save(cartItem);
        return new SuccessResult("Sepetteki ürün güncellendi.");
    }

    @Override
    public Result delete(DeleteCartItemRequest deleteCartItemRequest) {
        cartItemsRepository.deleteById(deleteCartItemRequest.getCartItemId());
        return new SuccessResult("Sepetteki ürün silindi.");
    }

    @Override
    public DataResult<List<CartItemListResponse>> getAll() {
        List<CartItem> cartItems = this.cartItemsRepository.findAll();
        List<CartItemListResponse> response = cartItems.stream().map(cartItem -> this.modelMapperService.forResponse().map(cartItem, CartItemListResponse.class)).collect(Collectors.toList());
        for (int i = 0; i < cartItems.size(); i++) {
            response.get(i).setCartId(cartItems.get(i).getCart().getCartId());
        }
        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<CartItemListResponse> getById(int cartItemId) {
        CartItem cartItem = this.cartItemsRepository.findById(cartItemId).orElseThrow(() -> new BusinessException("Verilen id ile sepet ürünü bulunamadı"));
        CartItemListResponse response = this.modelMapperService.forResponse().map(cartItem, CartItemListResponse.class);
        return new SuccessDataResult<>(response);
    }
}
