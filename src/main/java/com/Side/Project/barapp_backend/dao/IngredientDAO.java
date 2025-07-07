package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.Ingredient;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface IngredientDAO extends ListCrudRepository<Ingredient, Long> {
    List<Ingredient> findByIsAvailableTrue();

    List<Ingredient> findByNameContainingIgnoreCase(String name);
}
