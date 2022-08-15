package com.etiya.northwind.business.concretes;

import com.etiya.northwind.TestSupport;
import com.etiya.northwind.business.requests.productRequests.CreateProductRequest;
import com.etiya.northwind.business.requests.productRequests.UpdateProductRequest;
import com.etiya.northwind.business.responses.products.ProductListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.ProductRepository;
import com.etiya.northwind.entities.concretes.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

class ProductManagerTest {

    ModelMapperService modelMapperService;
    ProductRepository mockProductRepository;
    ProductManager productManager;

    @BeforeEach
    void setup() {

        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockProductRepository = mock(ProductRepository.class);
        productManager = new ProductManager(mockProductRepository, modelMapperService);
    }

    @Test
    void addTest_whenProductExists_dontAllowDuplicates() {

        CreateProductRequest productRequest = CreateProductRequest.builder().productId(1).build();

        when(mockProductRepository.existsById(productRequest.getProductId())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, ()-> productManager.add(productRequest));

    }

    @Test
    void updateTest_whenProductDoesntExist_throwBusinessException(){
        Product product = Product.builder().productId(1).build();
        UpdateProductRequest updateProductRequest = this.modelMapperService.forRequest().map(product, UpdateProductRequest.class);
        when(mockProductRepository.existsById(updateProductRequest.getProductId())).thenReturn(false);
        Assertions.assertThrows(BusinessException.class, ()-> productManager.update(updateProductRequest));
    }

    @Test
    void updateTest_whenProductIsUpdated_assertDataIntegrity() {
        Product product = Product.builder().productId(1).build();
        when(mockProductRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        UpdateProductRequest updateRequest = this.modelMapperService.forRequest().map(product, UpdateProductRequest.class);
        productManager.update(updateRequest);

        ProductListResponse expected = this.modelMapperService.forResponse().map(product, ProductListResponse.class);
        ProductListResponse actual = productManager.getById(product.getProductId()).getData();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllUsersTest_itShouldReturnUserDtoList() {
        List<Product> productList = TestSupport.generateProducts();
        when(mockProductRepository.findAll()).thenReturn(productList);

        List<ProductListResponse> productResponseList =
                productList.stream()
                        .map(c -> this.modelMapperService
                                .forResponse()
                                .map(c, ProductListResponse.class))
                        .collect(Collectors.toList());
        DataResult<List<ProductListResponse>> expected = new SuccessDataResult<>(productResponseList);

        DataResult<List<ProductListResponse>> actual = productManager.getAll();

        Assertions.assertEquals(expected,actual);
        Mockito.verify(mockProductRepository).findAll();

    }

    @Test
    void getByIdTest_whenProductIdDoesntExist_shouldThrowBusinessException() {

        when(mockProductRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> productManager.getById(1));
    }

    @Test
    void getByIdTest_whenProductIdExists_shouldReturnProductWithThatId() {
        Product product = Product.builder().productId(1).build();
        when(mockProductRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        ProductListResponse productListResponse = this.modelMapperService.forResponse().map(product, ProductListResponse.class);
        DataResult<ProductListResponse> expected = new SuccessDataResult<>(productListResponse);

        DataResult<ProductListResponse> actual = productManager.getById(product.getProductId());

        Assertions.assertEquals(expected, actual);
    }
}