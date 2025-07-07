package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.Ingredient;
import com.Side.Project.barapp_backend.dao.IngredientDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

	private final IngredientDAO ingredientDAO;

	public IngredientService(IngredientDAO ingredientDAO) {
		this.ingredientDAO = ingredientDAO;
	}

	/**
	 * Get all ingredients
	 */
	public List<Ingredient> getAllIngredients() {
		return ingredientDAO.findAll();
	}

	/**
	 * Get available ingredients only
	 */
	public List<Ingredient> getAvailableIngredients() {
		return ingredientDAO.findByIsAvailableTrue();
	}

	/**
	 * Get ingredient by ID
	 */
	public Optional<Ingredient> getIngredientById(Long id) {
		return ingredientDAO.findById(id);
	}

	/**
	 * Search ingredients by name
	 */
	public List<Ingredient> searchIngredients(String name) {
		return ingredientDAO.findByNameContainingIgnoreCase(name);
	}

	/**
	 * Create new ingredient
	 */
	public Ingredient createIngredient(Ingredient ingredient) {
		return ingredientDAO.save(ingredient);
	}

	/**
	 * Update ingredient
	 */
	public Ingredient updateIngredient(Long id, Ingredient ingredientDetails) {
		return ingredientDAO.findById(id)
				.map(ingredient -> {
					ingredient.setName(ingredientDetails.getName());
					ingredient.setPrice(ingredientDetails.getPrice());
					ingredient.setIsAvailable(ingredientDetails.getIsAvailable());
					return ingredientDAO.save(ingredient);
				})
				.orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
	}

	/**
	 * Toggle ingredient availability
	 */
	public Ingredient toggleAvailability(Long id) {
		return ingredientDAO.findById(id)
				.map(ingredient -> {
					ingredient.setIsAvailable(!ingredient.getIsAvailable());
					return ingredientDAO.save(ingredient);
				})
				.orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
	}

	/**
	 * Delete ingredient
	 */
	public void deleteIngredient(Long id) {
		ingredientDAO.deleteById(id);
	}
}
