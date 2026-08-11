package com.futureretail.models.repositories;

import com.futureretail.models.entites.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.sku IN :skus AND p.status = 'ACTIVE'")
    List<Product> findActiveProductsBySkus(@Param("skus") List<String> skus);
}
