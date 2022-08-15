package com.etiya.northwind.business.abstracts;

import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.productRequests.CreateProductRequest;
import com.etiya.northwind.business.requests.productRequests.UpdateProductRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.products.ProductListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;

import java.util.List;

public interface ProductService {
    Result add(CreateProductRequest createProductRequest);
    Result update(UpdateProductRequest updateProductRequest);
    Result delete(int productId);
    DataResult<List<ProductListResponse>> getAll();
    DataResult<ProductListResponse> getById(int productId);

    DataResult<PageDataResponse<ProductListResponse>> getByPage(PageDataRequest pageDataRequest);

    DataResult<PageDataResponse<ProductListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest);
}
