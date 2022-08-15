package com.etiya.northwind.business.abstracts;

import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.supplierRequests.CreateSupplierRequest;
import com.etiya.northwind.business.requests.supplierRequests.UpdateSupplierRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.suppliers.SupplierListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;

import java.util.List;

public interface SupplierService {
    Result add(CreateSupplierRequest createSupplierRequest);
    Result update(UpdateSupplierRequest updateSupplierRequest);
    Result delete(int supplierId);
    DataResult<List<SupplierListResponse>> getAll();
    DataResult<SupplierListResponse> getById(int supplierId);

    DataResult<PageDataResponse<SupplierListResponse>> getByPage (PageDataRequest pageDataRequest);

    DataResult<PageDataResponse<SupplierListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest);
}
