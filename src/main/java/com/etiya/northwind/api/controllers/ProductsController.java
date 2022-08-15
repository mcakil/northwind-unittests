package com.etiya.northwind.api.controllers;

import com.etiya.northwind.business.abstracts.ProductService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.productRequests.CreateProductRequest;
import com.etiya.northwind.business.requests.productRequests.UpdateProductRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.products.ProductListResponse;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getall")
    public DataResult<List<ProductListResponse>> getAll(){
        return this.productService.getAll();
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateProductRequest createProductRequest){
        return this.productService.add(createProductRequest);
    }

    @PostMapping("/update")
    public Result update(@RequestBody @Valid UpdateProductRequest updateProductRequest){
        return this.productService.update(updateProductRequest);
    }

    @DeleteMapping("/delete/{productId}")
    public Result delete(@Valid @PathVariable int productId){
        return this.productService.delete(productId);
    }

    @GetMapping("/getbyid/{productId}")
    public DataResult<ProductListResponse> getById(@PathVariable int productId){
        return this.productService.getById(productId);
    }

    @PostMapping("/getByPage")
    public DataResult<PageDataResponse<ProductListResponse>> getByPage(@RequestBody @Valid PageDataRequest pageDataRequest){
        return this.productService.getByPage(pageDataRequest);
    }

    @PostMapping("/getByPageWithSorting")
    public DataResult<PageDataResponse<ProductListResponse>> getByPageWithSorting(@RequestBody @Valid PageSortRequest pageSortRequest){
        return this.productService.getByPageWithSorting(pageSortRequest);
    }
}
