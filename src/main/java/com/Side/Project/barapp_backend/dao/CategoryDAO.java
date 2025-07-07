package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.Category;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CategoryDAO extends ListCrudRepository<Category, Long> {
    List<Category> findByParentIsNull(); // Catégories principal
    List<Category> findByParentId(Long parentId); // Sous-catégories
    List<Category> findByNameContainingIgnoreCase(String name);
}