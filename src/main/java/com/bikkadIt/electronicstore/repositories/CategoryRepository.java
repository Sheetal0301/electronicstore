package com.bikkadIt.electronicstore.repositories;

import com.bikkadIt.electronicstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
