package com.etiya.northwind.api.controllers;

import com.etiya.northwind.business.abstracts.OrderDetailsService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.CreateOrderDetailRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.UpdateOrderDetailRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.orderDetails.OrderDetailsListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.entities.concretes.OrderDetailsId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orderDetails")
public class OrderDetailsController {
    private OrderDetailsService orderDetailsService;

    @Autowired
    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping("/getall")
    public DataResult<List<OrderDetailsListResponse>> getAll(){
        return this.orderDetailsService.getAll();
    }


    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateOrderDetailRequest createOrderDetailRequest){
        return this.orderDetailsService.add(createOrderDetailRequest);
    }

    @PostMapping("/update")
    public Result update(@RequestBody @Valid UpdateOrderDetailRequest updateOrderDetailRequest){
        return this.orderDetailsService.update(updateOrderDetailRequest);
    }

    @DeleteMapping("/delete/{orderDetailsId}")
    public Result delete(@Valid @PathVariable OrderDetailsId orderDetailsId){
        return this.orderDetailsService.delete(orderDetailsId);
    }

    @GetMapping("/getbyid/{orderDetailsId}")
    public DataResult<OrderDetailsListResponse> getById(@PathVariable OrderDetailsId orderDetailsId){
        return this.orderDetailsService.getById(orderDetailsId);
    }


    @PostMapping("/getByPage")
    public DataResult<PageDataResponse<OrderDetailsListResponse>> getByPage(@RequestBody @Valid PageDataRequest pageDataRequest){
        return this.orderDetailsService.getByPage(pageDataRequest);
    }

    @PostMapping("/getByPageWithSorting")
    public DataResult<PageDataResponse<OrderDetailsListResponse>> getByPageWithSorting(@RequestBody @Valid PageSortRequest pageSortRequest){
        return this.orderDetailsService.getByPageWithSorting(pageSortRequest);
    }
}
