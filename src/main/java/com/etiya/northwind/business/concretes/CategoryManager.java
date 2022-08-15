package com.etiya.northwind.business.concretes;

import com.etiya.northwind.business.abstracts.CategoryService;
import com.etiya.northwind.business.requests.PageDataRequest;
import com.etiya.northwind.business.requests.PageSortRequest;
import com.etiya.northwind.business.requests.categoryRequests.CreateCategoryRequest;
import com.etiya.northwind.business.requests.categoryRequests.UpdateCategoryRequest;
import com.etiya.northwind.business.responses.PageDataResponse;
import com.etiya.northwind.business.responses.categories.CategoryListResponse;
import com.etiya.northwind.core.exceptions.BusinessException;
import com.etiya.northwind.core.helpers.PageableSorter;
import com.etiya.northwind.core.mapping.ModelMapperService;
import com.etiya.northwind.core.results.DataResult;
import com.etiya.northwind.core.results.Result;
import com.etiya.northwind.core.results.SuccessDataResult;
import com.etiya.northwind.core.results.SuccessResult;
import com.etiya.northwind.dataAccess.abstracts.CategoryRepository;
import com.etiya.northwind.entities.concretes.Category;
import com.etiya.northwind.entities.concretes.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryManager implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapperService modelMapperService;

    @Autowired
    public CategoryManager(CategoryRepository categoryRepository, ModelMapperService modelMapperService) {
        this.categoryRepository = categoryRepository;
        this.modelMapperService = modelMapperService;
    }


    @Override
    public Result add(CreateCategoryRequest createCategoryRequest) {
        checkCategoryExists(createCategoryRequest.getCategoryId());
        checkIfCategoryNameExists(createCategoryRequest.getCategoryName());
        Category category = this.modelMapperService.forRequest().map(createCategoryRequest, Category.class);
        categoryRepository.save(category);
        return new SuccessResult("Added");
    }

    @Override
    public Result update(UpdateCategoryRequest updateCategoryRequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(updateCategoryRequest.getCategoryId());
        optionalCategory.ifPresentOrElse(category ->
                {
                    category = this.modelMapperService
                            .forRequest()
                            .map(updateCategoryRequest, Category.class);
                    categoryRepository.save(category);
                },
                () -> {
                    throw new BusinessException("category not found");
                });


        return new SuccessResult("Updated");
    }

    @Override
    public Result delete(int categoryId) {
        checkCategoryExists(categoryId);
        this.categoryRepository.deleteById(categoryId);
        return new SuccessResult("Deleted successfully.");
    }

    @Override
    public DataResult<List<CategoryListResponse>> getAll() {
        List<Category> result = this.categoryRepository.findAll();
        List<CategoryListResponse> response = result.stream()
                .map(category -> this.modelMapperService.forResponse()
                        .map(category, CategoryListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<CategoryListResponse> getById(int categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessException("Category not found."));
        CategoryListResponse response = this.modelMapperService.forResponse().map(category, CategoryListResponse.class);

        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult<PageDataResponse<CategoryListResponse>> getByPage(PageDataRequest pageDataRequest) {
        Pageable pageable = PageRequest.of(pageDataRequest.getNumber() -1, pageDataRequest.getItemAmount());
        return new SuccessDataResult<>(getPageDataResponse(pageDataRequest.getNumber(), pageable));
    }

    @Override
    public DataResult<PageDataResponse<CategoryListResponse>> getByPageWithSorting(PageSortRequest pageSortRequest) {
        return new SuccessDataResult<>(getPageDataResponse(pageSortRequest.getNumber(), PageableSorter.getSortedPageable(pageSortRequest)));
    }

    private PageDataResponse<CategoryListResponse> getPageDataResponse(int pageNumber, Pageable pageable) {
        Page<Category> pages = this.categoryRepository.findAllCategories(pageable);
        List<CategoryListResponse> response =
                pages.getContent().stream().map(category -> this.modelMapperService.forResponse().map(category, CategoryListResponse.class)).collect(Collectors.toList());

        return new PageDataResponse<>(response, pages.getTotalPages(), pages.getTotalElements(), pageNumber);
    }

    private void checkCategoryExists(int categoryId) {
        if (categoryRepository.existsById(categoryId)){
            throw new BusinessException("Category does not exist.");
        }
    }

    private void checkIfCategoryNameExists(String categoryName) {
        if (this.categoryRepository.isCategoryNameExists(categoryName)){
            throw new BusinessException("Category name already exists in the database.");
        }
    }

}
