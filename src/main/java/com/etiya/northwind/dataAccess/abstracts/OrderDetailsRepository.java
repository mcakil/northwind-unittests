package com.etiya.northwind.dataAccess.abstracts;

import com.etiya.northwind.entities.concretes.Category;
import com.etiya.northwind.entities.concretes.OrderDetails;
import com.etiya.northwind.entities.concretes.OrderDetailsId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsId> {
    @Query("select a from OrderDetails a")
    Page<OrderDetails> findAllOrderDetails(Pageable pageable);
}
