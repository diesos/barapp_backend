package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.Recipe;
import com.Side.Project.barapp_backend.models.Cocktail;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RecipeDAO extends ListCrudRepository<Recipe, Long> {
    List<Recipe> findByCocktail(Cocktail cocktail);

    List<Recipe> findByCocktail_Id(Long cocktailId); // CORRIGER: utiliser cocktail_Id

    List<Recipe> findByNameContainingIgnoreCase(String name);
}
