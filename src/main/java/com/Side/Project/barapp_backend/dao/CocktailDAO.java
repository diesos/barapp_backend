package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.Cocktail;
import com.Side.Project.barapp_backend.models.Category;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CocktailDAO extends ListCrudRepository<Cocktail, Long> {
    List<Cocktail> findByIsVisibleTrueAndIsAvailableTrue();
    List<Cocktail> findByCategory(Category category);
    List<Cocktail> findByCategoryId(Long categoryId);
    List<Cocktail> findByNameContainingIgnoreCase(String name);
    List<Cocktail> findByIsDiscountTrue();
}
