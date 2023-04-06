package com.bikkadIt.electronicstore.repositories;

import com.bikkadIt.electronicstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByTitleContaining(String title);

    List<Product> findByLive(boolean live);
}
