package com.etiya.northwind.api.controllers;


import com.etiya.northwind.business.abstracts.OrderService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.orderRequests.CreateOrderRequest;
import com.etiya.northwind.business.requests.orderRequests.UpdateOrderRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.orders.OrderListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private OrderService orderService;


    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getall")
    public DataResult<List<OrderListResponse>> getAll() {
        return this.orderService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateOrderRequest createOrderRequest) {

        return this.orderService.add(createOrderRequest);
    }

    @PostMapping("/update")
    public Result update(@RequestBody @Valid UpdateOrderRequest updateOrderRequest) {
        return this.orderService.update(updateOrderRequest);
    }

    @DeleteMapping("/delete/{orderId}")
    public Result delete(@Valid @PathVariable int orderId) {
        return this.orderService.delete(orderId);
    }

    @GetMapping("/getbyid/{orderId}")
    public DataResult<OrderListResponse> getById(@PathVariable int orderId) {
        return this.orderService.getById(orderId);
    }

    @PostMapping("/getByPage")
    public DataResult<PageDataResponse<OrderListResponse>> getByPage(@RequestBody @Valid PageDataRequest pageDataRequest) {
        return this.orderService.getByPage(pageDataRequest);
    }

    @PostMapping("/getByPageWithSorting")
    public DataResult<PageDataResponse<OrderListResponse>> getByPageWithSorting(@RequestBody @Valid PageSortRequest pageSortRequest) {
        return this.orderService.getByPageWithSorting(pageSortRequest);
    }
}
