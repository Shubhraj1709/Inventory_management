package com.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
