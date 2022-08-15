package com.etiya.northwind.business.concretes;

import com.etiya.northwind.TestSupport;
import com.etiya.northwind.business.requests.supplierRequests.CreateSupplierRequest;
import com.etiya.northwind.business.requests.supplierRequests.UpdateSupplierRequest;
import com.etiya.northwind.business.responses.suppliers.SupplierListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.SupplierRepository;
import com.etiya.northwind.entities.concretes.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SupplierManagerTest {

    ModelMapperService modelMapperService;
    SupplierRepository mockSupplierRepository;
    SupplierManager supplierManager;

    @BeforeEach
    void setup() {

        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockSupplierRepository = mock(SupplierRepository.class);
        supplierManager = new SupplierManager(mockSupplierRepository, modelMapperService);
    }

    @Test
    void addTest_whenSupplierExists_dontAllowDuplicates() {

        CreateSupplierRequest supplierRequest = new CreateSupplierRequest(
                1, "TEST","Test","Test"
        );

        when(mockSupplierRepository.existsById(supplierRequest.getSupplierId())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, ()-> supplierManager.add(supplierRequest));

    }

    @Test
    void updateTest_whenSupplierDoesntExist_throwBusinessException(){
        Supplier supplier = new Supplier(1, "TEST","Test","Test");
        UpdateSupplierRequest updateSupplierRequest = this.modelMapperService.forRequest().map(supplier, UpdateSupplierRequest.class);
        when(mockSupplierRepository.existsById(updateSupplierRequest.getSupplierId())).thenReturn(false);
        Assertions.assertThrows(BusinessException.class, ()-> supplierManager.update(updateSupplierRequest));
    }

    @Test
    void updateTest_whenSupplierIsUpdated_assertDataIntegrity() {
        Supplier supplier = new Supplier(1, "TEST","Test","Test");
        when(mockSupplierRepository.findById(supplier.getSupplierId())).thenReturn(Optional.of(supplier));
        UpdateSupplierRequest updateRequest = this.modelMapperService.forRequest().map(supplier, UpdateSupplierRequest.class);
        supplierManager.update(updateRequest);

        SupplierListResponse expected = this.modelMapperService.forResponse().map(supplier, SupplierListResponse.class);
        SupplierListResponse actual = supplierManager.getById(supplier.getSupplierId()).getData();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllUsersTest_itShouldReturnUserDtoList() {
        List<Supplier> supplierList = TestSupport.generateSuppliers();
        when(mockSupplierRepository.findAll()).thenReturn(supplierList);

        List<SupplierListResponse> supplierResponseList =
                supplierList.stream()
                        .map(c -> this.modelMapperService
                                .forResponse()
                                .map(c, SupplierListResponse.class))
                        .collect(Collectors.toList());
        DataResult<List<SupplierListResponse>> expected = new SuccessDataResult<>(supplierResponseList);

        DataResult<List<SupplierListResponse>> actual = supplierManager.getAll();

        Assertions.assertEquals(expected,actual);
        Mockito.verify(mockSupplierRepository).findAll();

    }

    @Test
    void getByIdTest_whenSupplierIdDoesntExist_shouldThrowBusinessException() {

        when(mockSupplierRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> supplierManager.getById(1));
    }

    @Test
    void getByIdTest_whenSupplierIdExists_shouldReturnSupplierWithThatId() {
        Supplier supplier = new Supplier(1, "TEST","Test","Test");
        when(mockSupplierRepository.findById(supplier.getSupplierId())).thenReturn(Optional.of(supplier));
        SupplierListResponse supplierListResponse = this.modelMapperService.forResponse().map(supplier, SupplierListResponse.class);
        DataResult<SupplierListResponse> expected = new SuccessDataResult<>(supplierListResponse);

        DataResult<SupplierListResponse> actual = supplierManager.getById(supplier.getSupplierId());

        Assertions.assertEquals(expected, actual);
    }
}