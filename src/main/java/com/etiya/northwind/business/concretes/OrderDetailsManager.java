package com.etiya.northwind.business.concretes;
import com.etiya.northwind.business.abstracts.OrderDetailsService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.CreateOrderDetailRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.UpdateOrderDetailRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.orderDetails.OrderDetailsListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.helpers.PageableSorter;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.core.results.SuccessResult;
import com.etiya.northwind.dataAccess.abstracts.OrderDetailsRepository;
import com.etiya.northwind.entities.concretes.*;
import com.etiya.northwind.entities.concretes.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailsManager implements OrderDetailsService {
    private OrderDetailsRepository orderDetailsRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public OrderDetailsManager(OrderDetailsRepository orderDetailsRepository, ModelMapperService modelMapperService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.modelMapperService = modelMapperService;
    }

    /*public void save(OrderDetails orderDetails){
        orderDetailsRepository.save(orderDetails);
    }*/

    @Override
    public Result add(CreateOrderDetailRequest createOrderDetailsRequest) {
        OrderDetails orderDetails = this.modelMapperService.forRequest().map(createOrderDetailsRequest, OrderDetails.class);
        orderDetailsRepository.save(orderDetails);
        return new SuccessResult("Added");
    }

    @Override
    public Result update(UpdateOrderDetailRequest updateOrderDetailsRequest) {
        OrderDetails orderDetails = this.modelMapperService.forRequest().map(updateOrderDetailsRequest, OrderDetails.class);
        orderDetailsRepository.save(orderDetails);
        return new SuccessResult("Updated");
    }

    @Override
    public Result delete(OrderDetailsId orderDetailsId) {
        checkOrderDetailsExists(orderDetailsId);
        this.orderDetailsRepository.deleteById(orderDetailsId);
        return new SuccessResult("Deleted successfully.");
    }

    @Override
    public DataResult<List<OrderDetailsListResponse>> getAll() {
        List<OrderDetails> result = this.orderDetailsRepository.findAll();
        List<OrderDetailsListResponse> response = result.stream()
                .map(orderDetails -> this.modelMapperService.forResponse()
                        .map(orderDetails, OrderDetailsListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response);
    }


    @Override
    public DataResult<OrderDetailsListResponse> getById(OrderDetailsId orderDetailsId) {
        OrderDetails orderDetails = this.orderDetailsRepository.findById(orderDetailsId).orElseThrow(() -> new BusinessException("OrderDetails not found."));
        OrderDetailsListResponse response = this.modelMapperService.forResponse().map(orderDetails, OrderDetailsListResponse.class);

        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<PageDataResponse<OrderDetailsListResponse>> getByPage(PageDataRequest pageDataRequest) {
        Pageable pageable = PageRequest.of(pageDataRequest.getNumber()-1, pageDataRequest.getItemAmount());
        return new SuccessDataResult<>(getPageDataResponse(pageDataRequest.getNumber(), pageable));
    }

    @Override
    public DataResult<PageDataResponse<OrderDetailsListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest) {
        return new SuccessDataResult<>(getPageDataResponse(pageSortRequest.getNumber(), PageableSorter.getSortedPageable(pageSortRequest)));
    }

    private PageDataResponse<OrderDetailsListResponse> getPageDataResponse(int pageNumber, Pageable pageable) {
        Page<OrderDetails> pages = this.orderDetailsRepository.findAllOrderDetails(pageable);
        List<OrderDetailsListResponse> response =
                pages.getContent().stream().map(orderDetails -> this.modelMapperService.forResponse().map(orderDetails, OrderDetailsListResponse.class)).collect(Collectors.toList());

        return new PageDataResponse<OrderDetailsListResponse>(response, pages.getTotalPages(), pages.getTotalElements(), pageNumber);
    }

    private void checkOrderDetailsExists(OrderDetailsId orderDetailsId) {
        if (!orderDetailsRepository.existsById(orderDetailsId)){
            throw new BusinessException("Order detail does not exist.");
        }
    }
}