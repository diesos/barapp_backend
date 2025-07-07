package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.RecipeIngredient;
import com.Side.Project.barapp_backend.models.Recipe;
import com.Side.Project.barapp_backend.models.Ingredient;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeIngredientDAO extends ListCrudRepository<RecipeIngredient, Long> {
	List<RecipeIngredient> findByRecipe(Recipe recipe);

	List<RecipeIngredient> findByRecipeId(Long recipeId);

	Optional<RecipeIngredient> findByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

	void deleteByRecipe(Recipe recipe);
}
