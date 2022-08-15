package com.etiya.northwind.business.concretes;

import com.etiya.northwind.business.abstracts.CartItemService;
import com.etiya.northwind.business.abstracts.CartService;
import com.etiya.northwind.business.abstracts.OrderDetailsService;
import com.etiya.northwind.business.abstracts.OrderService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.CreateOrderDetailRequest;
import com.etiya.northwind.business.requests.orderRequests.CreateOrderRequest;
import com.etiya.northwind.business.requests.orderRequests.UpdateOrderRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.orders.OrderListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.helpers.PageableSorter;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.core.results.SuccessResult;
import com.etiya.northwind.dataAccess.abstracts.*;
import com.etiya.northwind.entities.concretes.Cart;
import com.etiya.northwind.entities.concretes.CartItem;
import com.etiya.northwind.entities.concretes.Order;
import com.etiya.northwind.entities.concretes.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderManager implements OrderService {
    private OrderRepository orderRepository;
    private ModelMapperService modelMapperService;
    private OrderDetailsService orderDetailsService;
    private final CartService cartService;
    private final CartItemService cartItemService;

    @Autowired
    public OrderManager(OrderRepository orderRepository, ModelMapperService modelMapperService, OrderDetailsService orderDetailsService, CartService cartService, CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.modelMapperService = modelMapperService;
        this.orderDetailsService = orderDetailsService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @Override
    public Result add(CreateOrderRequest createOrderRequest) {

        Order order = this.modelMapperService.forRequest().map(createOrderRequest, Order.class);
        Cart cart = cartService.getCart(createOrderRequest.getCustomerId());
        orderRepository.save(order);
        List<CartItem> cartItems = cart.getCartItems();

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest(
                    order.getOrderId(),
                    cartItem.getProduct().getProductId(),
                    cartItem.getUnitPrice(),
                    cartItem.getQuantity(),
                    0.0
            );
            orderDetailsService.add(createOrderDetailRequest);
        }

        cartItemService.deleteCartItemsByCartId(cart.getCartId());

        return new SuccessResult();
    }


    @Override
    public Result update(UpdateOrderRequest updateOrderRequest) {
        Order order = this.modelMapperService.forRequest().map(updateOrderRequest, Order.class);
        orderRepository.save(order);
        return new SuccessResult();
    }

    @Override
    public Result delete(int orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            System.out.println("Gecersiz Siparis ID");
        }
        return new SuccessResult();
    }

    @Override
    public DataResult<List<OrderListResponse>> getAll() {
        List<Order> result = this.orderRepository.findAll();
        List<OrderListResponse> response =
                result.stream().map(order -> this.modelMapperService.forResponse().map(order, OrderListResponse.class)).collect(Collectors.toList());

        for (int i = 0; i < result.size(); i++) {
            response.get(i).setEmployeeName(result.get(i).getEmployee().getFirstName() + " " + result.get(i).getEmployee().getLastName());
        }
        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<OrderListResponse> getById(int orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new BusinessException("Order not found."));
        OrderListResponse response = this.modelMapperService.forResponse().map(order, OrderListResponse.class);

        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<PageDataResponse<OrderListResponse>> getByPage(PageDataRequest pageDataRequest) {
        Pageable pageable = PageRequest.of(pageDataRequest.getNumber()- 1, pageDataRequest.getItemAmount());
        return new SuccessDataResult<>(getPageDataResponse(pageDataRequest.getNumber(), pageable));
    }

    @Override
    public DataResult<PageDataResponse<OrderListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest) {
        return new SuccessDataResult<>(getPageDataResponse(pageSortRequest.getNumber(), PageableSorter.getSortedPageable(pageSortRequest)));
    }

    private PageDataResponse<OrderListResponse> getPageDataResponse(int pageNumber, Pageable pageable) {
        Page<Order> pages = this.orderRepository.findAllOrders(pageable);
        List<OrderListResponse> response =
                pages.getContent().stream().map(order -> this.modelMapperService.forResponse().map(order, OrderListResponse.class)).collect(Collectors.toList());

        return new PageDataResponse<>(response, pages.getTotalPages(), pages.getTotalElements(), pageNumber);
    }
}

