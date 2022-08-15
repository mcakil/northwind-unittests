package com.etiya.northwind.business.concretes;

import com.etiya.northwind.TestSupport;
import com.etiya.northwind.business.requests.categoryRequests.CreateCategoryRequest;
import com.etiya.northwind.business.requests.categoryRequests.UpdateCategoryRequest;
import com.etiya.northwind.business.responses.categories.CategoryListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.mapping.ModelMapperManager;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.dataAccess.abstracts.CategoryRepository;
import com.etiya.northwind.entities.concretes.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CategoryManagerTest {

    ModelMapperService modelMapperService;
    CategoryRepository mockCategoryRepository;
    CategoryManager categoryManager;

    @BeforeEach
    void setup() {

        modelMapperService = new ModelMapperManager(new ModelMapper());
        mockCategoryRepository = mock(CategoryRepository.class);
        categoryManager = new CategoryManager(mockCategoryRepository, modelMapperService);
    }

    @Test
    void addTest_whenCategoryExists_dontAllowDuplicates() {

        CreateCategoryRequest categoryRequest = CreateCategoryRequest.builder().categoryId(1).build();

        when(mockCategoryRepository.existsById(categoryRequest.getCategoryId())).thenReturn(true);

        Assertions.assertThrows(BusinessException.class, ()-> categoryManager.add(categoryRequest));

    }

    @Test
    void updateTest_whenCategoryDoesntExist_throwBusinessException(){
        Category category = Category.builder().categoryId(1).build();
        UpdateCategoryRequest updateCategoryRequest = this.modelMapperService.forRequest().map(category, UpdateCategoryRequest.class);
        when(mockCategoryRepository.existsById(updateCategoryRequest.getCategoryId())).thenReturn(false);
        Assertions.assertThrows(BusinessException.class, ()-> categoryManager.update(updateCategoryRequest));
    }

    @Test
    void updateTest_whenCategoryIsUpdated_assertDataIntegrity() {
        Category category = Category.builder().categoryId(1).build();
        when(mockCategoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        UpdateCategoryRequest updateRequest = this.modelMapperService.forRequest().map(category, UpdateCategoryRequest.class);
        categoryManager.update(updateRequest);

        CategoryListResponse expected = this.modelMapperService.forResponse().map(category, CategoryListResponse.class);
        CategoryListResponse actual = categoryManager.getById(category.getCategoryId()).getData();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllUsersTest_itShouldReturnUserDtoList() {
        List<Category> categoryList = TestSupport.generateCategories();
        when(mockCategoryRepository.findAll()).thenReturn(categoryList);

        List<CategoryListResponse> categoryResponseList =
                categoryList.stream()
                        .map(c -> this.modelMapperService
                                .forResponse()
                                .map(c, CategoryListResponse.class))
                        .collect(Collectors.toList());
        DataResult<List<CategoryListResponse>> expected = new SuccessDataResult<>(categoryResponseList);

        DataResult<List<CategoryListResponse>> actual = categoryManager.getAll();

        Assertions.assertEquals(expected,actual);
        Mockito.verify(mockCategoryRepository).findAll();

    }

    @Test
    void getByIdTest_whenCategoryIdDoesntExist_shouldThrowBusinessException() {

        when(mockCategoryRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, ()-> categoryManager.getById(1));
    }

    @Test
    void getByIdTest_whenCategoryIdExists_shouldReturnCategoryWithThatId() {
        Category category = Category.builder().categoryId(1).build();
        when(mockCategoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        CategoryListResponse categoryListResponse = this.modelMapperService.forResponse().map(category, CategoryListResponse.class);
        DataResult<CategoryListResponse> expected = new SuccessDataResult<>(categoryListResponse);

        DataResult<CategoryListResponse> actual = categoryManager.getById(category.getCategoryId());

        Assertions.assertEquals(expected, actual);
    }
}