package com.etiya.northwind.business.concretes;

import com.etiya.northwind.TestSupport;
import com.etiya.northwind.business.requests.cartRequests.CreateCartRequest;
import com.etiya.northwind.business.responses.carts.CartListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.CartRepository;
import com.etiya.northwind.dataAccess.abstracts.CustomerRepository;
import com.etiya.northwind.entities.concretes.Cart;
import com.etiya.northwind.entities.concretes.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

class CartManagerTest {

    ModelMapperService modelMapperService;
    CartRepository mockCartRepository;
    CustomerRepository mockCustomerRepository;
    CartManager cartManager;
    CustomerManager customerManager;

    @BeforeEach
    void setup() {

        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockCartRepository = mock(CartRepository.class);
        mockCustomerRepository = mock(CustomerRepository.class);
        customerManager = new CustomerManager(mockCustomerRepository, modelMapperService);
        cartManager = new CartManager(mockCartRepository, modelMapperService, customerManager);
    }

    @Test
    void addTest_whenCartExists_dontAllowDuplicates() {

        CreateCartRequest cartRequest = new CreateCartRequest("ANTON");

        Customer customer = Customer.builder().customerId(cartRequest.getCustomerId()).build();

        Cart cart = Cart.builder().customer(customer).build();

        when(mockCustomerRepository.findById(cartRequest.getCustomerId())).thenReturn(Optional.of(customer));
        when(mockCartRepository.getByCustomerId(cartRequest.getCustomerId())).thenReturn(Optional.of(cart));

        Assertions.assertThrows(BusinessException.class, ()-> cartManager.add(cartRequest));

    }

    @Test
    void getAllUsersTest_itShouldReturnUserDtoList() {
        List<Cart> cartList = TestSupport.generateCarts();
        when(mockCartRepository.findAll()).thenReturn(cartList);

        List<CartListResponse> cartResponseList =
                cartList.stream()
                        .map(c -> this.modelMapperService
                                .forResponse()
                                .map(c, CartListResponse.class))
                        .collect(Collectors.toList());
        DataResult<List<CartListResponse>> expected = new SuccessDataResult<>(cartResponseList);

        DataResult<List<CartListResponse>> actual = cartManager.getAll();

        Assertions.assertEquals(expected,actual);
        Mockito.verify(mockCartRepository).findAll();

    }

    @Test
    void getByIdTest_whenCartIdDoesntExist_shouldThrowBusinessException() {

        when(mockCartRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> cartManager.getById(1));
    }

    @Test
    void getByIdTest_whenCartIdExists_shouldReturnCartWithThatId() {
        Cart cart = Cart.builder().cartId(1).build();
        when(mockCartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
        CartListResponse cartListResponse = this.modelMapperService.forResponse().map(cart, CartListResponse.class);
        DataResult<CartListResponse> expected = new SuccessDataResult<>(cartListResponse);

        DataResult<CartListResponse> actual = cartManager.getById(cart.getCartId());

        Assertions.assertEquals(expected, actual);
    }
}