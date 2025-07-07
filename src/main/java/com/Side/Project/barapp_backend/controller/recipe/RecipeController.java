package com.Side.Project.barapp_backend.controller.recipe;

import com.Side.Project.barapp_backend.models.Recipe;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.service.RecipeService;
import com.Side.Project.barapp_backend.service.RecipeService.RecipeIngredientRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

	private final RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	/**
	 * Get all recipes
	 */
	@GetMapping
	public ResponseEntity<List<Recipe>> getAllRecipes() {
		List<Recipe> recipes = recipeService.getAllRecipes();
		return ResponseEntity.ok(recipes);
	}

	/**
	 * Get recipe by ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
		return recipeService.getRecipeById(id)
				.map(recipe -> ResponseEntity.ok(recipe))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Get recipes by cocktail
	 */
	@GetMapping("/cocktail/{cocktailId}")
	public ResponseEntity<List<Recipe>> getRecipesByCocktail(@PathVariable Long cocktailId) {
		List<Recipe> recipes = recipeService.getRecipesByCocktail(cocktailId);
		return ResponseEntity.ok(recipes);
	}

	/**
	 * Search recipes by name
	 */
	@GetMapping("/search")
	public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String name) {
		List<Recipe> recipes = recipeService.searchRecipes(name);
		return ResponseEntity.ok(recipes);
	}

	/**
	 * Create new recipe (barmaker/admin only)
	 */
	@PostMapping
	public ResponseEntity<Recipe> createRecipe(
			@RequestBody RecipeRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			Recipe createdRecipe = recipeService.createRecipe(
					request.getCocktailId(),
					request.getName(),
					request.getDescription(),
					request.getIngredients());
			return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	/**
	 * Update recipe (barmaker/admin only)
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Recipe> updateRecipe(
			@PathVariable Long id,
			@RequestBody RecipeRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			Recipe updatedRecipe = recipeService.updateRecipe(
					id,
					request.getName(),
					request.getDescription(),
					request.getIngredients());
			return ResponseEntity.ok(updatedRecipe);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Delete recipe (barmaker/admin only)
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRecipe(
			@PathVariable Long id,
			@AuthenticationPrincipal User user) {

		if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			recipeService.deleteRecipe(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Request DTO
	public static class RecipeRequest {
		private Long cocktailId;
		private String name;
		private String description;
		private List<RecipeIngredientRequest> ingredients;

		public RecipeRequest() {
		}

		public Long getCocktailId() {
			return cocktailId;
		}

		public void setCocktailId(Long cocktailId) {
			this.cocktailId = cocktailId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<RecipeIngredientRequest> getIngredients() {
			return ingredients;
		}

		public void setIngredients(List<RecipeIngredientRequest> ingredients) {
			this.ingredients = ingredients;
		}
	}
}
