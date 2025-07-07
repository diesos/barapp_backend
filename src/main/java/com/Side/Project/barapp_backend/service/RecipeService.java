package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.*;
import com.Side.Project.barapp_backend.dao.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

	private final RecipeDAO recipeDAO;
	private final RecipeIngredientDAO recipeIngredientDAO;
	private final CocktailDAO cocktailDAO;
	private final IngredientDAO ingredientDAO;

	public RecipeService(RecipeDAO recipeDAO, RecipeIngredientDAO recipeIngredientDAO,
			CocktailDAO cocktailDAO, IngredientDAO ingredientDAO) {
		this.recipeDAO = recipeDAO;
		this.recipeIngredientDAO = recipeIngredientDAO;
		this.cocktailDAO = cocktailDAO;
		this.ingredientDAO = ingredientDAO;
	}

	/**
	 * Get all recipes
	 */
	public List<Recipe> getAllRecipes() {
		return recipeDAO.findAll();
	}

	/**
	 * Get recipe by ID with ingredients
	 */
	public Optional<Recipe> getRecipeById(Long id) {
		Optional<Recipe> recipe = recipeDAO.findById(id);
		if (recipe.isPresent()) {
			// Force loading of recipe ingredients
			recipe.get().getRecipeIngredients().size();
		}
		return recipe;
	}

	/**
	 * Get recipes by cocktail
	 */
	public List<Recipe> getRecipesByCocktail(Long cocktailId) {
		return recipeDAO.findByCocktail_Id(cocktailId); // CORRIGER: utiliser cocktail_Id
	}

	/**
	 * Search recipes by name
	 */
	public List<Recipe> searchRecipes(String name) {
		return recipeDAO.findByNameContainingIgnoreCase(name);
	}

	/**
	 * Create recipe with ingredients
	 */
	@Transactional
	public Recipe createRecipe(Long cocktailId, String name, String description,
			List<RecipeIngredientRequest> ingredientRequests) {

		Cocktail cocktail = cocktailDAO.findById(cocktailId)
				.orElseThrow(() -> new RuntimeException("Cocktail not found"));

		// Create recipe
		Recipe recipe = new Recipe(name, description, cocktail);
		recipe = recipeDAO.save(recipe);

		// Add ingredients
		for (RecipeIngredientRequest request : ingredientRequests) {
			Ingredient ingredient = ingredientDAO.findById(request.getIngredientId())
					.orElseThrow(() -> new RuntimeException("Ingredient not found: " + request.getIngredientId()));

			RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, request.getQuantity());
			recipeIngredientDAO.save(recipeIngredient);
		}

		return getRecipeById(recipe.getId()).orElse(recipe);
	}

	/**
	 * Update recipe with ingredients
	 */
	@Transactional
	public Recipe updateRecipe(Long id, String name, String description,
			List<RecipeIngredientRequest> ingredientRequests) {

		Recipe recipe = recipeDAO.findById(id)
				.orElseThrow(() -> new RuntimeException("Recipe not found"));

		// Update recipe info
		recipe.setName(name);
		recipe.setDescription(description);
		recipe = recipeDAO.save(recipe);

		// Remove existing ingredients
		recipeIngredientDAO.deleteByRecipe(recipe);

		// Add new ingredients
		for (RecipeIngredientRequest request : ingredientRequests) {
			Ingredient ingredient = ingredientDAO.findById(request.getIngredientId())
					.orElseThrow(() -> new RuntimeException("Ingredient not found: " + request.getIngredientId()));

			RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, request.getQuantity());
			recipeIngredientDAO.save(recipeIngredient);
		}

		return getRecipeById(recipe.getId()).orElse(recipe);
	}

	/**
	 * Delete recipe
	 */
	@Transactional
	public void deleteRecipe(Long id) {
		Recipe recipe = recipeDAO.findById(id)
				.orElseThrow(() -> new RuntimeException("Recipe not found"));

		recipeIngredientDAO.deleteByRecipe(recipe);
		recipeDAO.delete(recipe);
	}

	// Inner class for recipe ingredient requests
	public static class RecipeIngredientRequest {
		private Long ingredientId;
		private Integer quantity;

		public RecipeIngredientRequest() {
		}

		public RecipeIngredientRequest(Long ingredientId, Integer quantity) {
			this.ingredientId = ingredientId;
			this.quantity = quantity;
		}

		public Long getIngredientId() {
			return ingredientId;
		}

		public void setIngredientId(Long ingredientId) {
			this.ingredientId = ingredientId;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}
}
