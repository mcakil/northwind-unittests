package com.etiya.northwind.dataAccess.abstracts;

import com.etiya.northwind.entities.concretes.Category;
import com.etiya.northwind.entities.concretes.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select a from Category a")
    Page<Category> findAllCategories(Pageable pageable);

    @Query("select case when count(c) > 0 then true else false end from Category c where lower(c.categoryName) like lower(:categoryName)")
    boolean isCategoryNameExists(@Param("categoryName") String categoryName);

}
