package com.etiya.northwind.business.abstracts;

import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.CreateOrderDetailRequest;
import com.etiya.northwind.business.requests.orderDetailRequests.UpdateOrderDetailRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.orderDetails.OrderDetailsListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.entities.concretes.OrderDetails;
import com.etiya.northwind.entities.concretes.OrderDetailsId;

import java.util.List;

public interface OrderDetailsService {
    Result add(CreateOrderDetailRequest createOrderDetailsRequest);
    Result update(UpdateOrderDetailRequest updateOrderDetailsRequest);
    Result delete(OrderDetailsId orderDetailId);
    public void save(OrderDetails orderDetails);
    DataResult<List<OrderDetailsListResponse>> getAll();
    DataResult<OrderDetailsListResponse> getById(OrderDetailsId orderDetailsId);

    DataResult<PageDataResponse<OrderDetailsListResponse>> getByPage(PageDataRequest pageDataRequest);

    DataResult<PageDataResponse<OrderDetailsListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest);

}
