package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.Cocktail;
import com.Side.Project.barapp_backend.dao.CocktailDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CocktailService {

    private final CocktailDAO cocktailDAO;

    public CocktailService(CocktailDAO cocktailDAO) {
        this.cocktailDAO = cocktailDAO;
    }

    /**
     * Get all available cocktails for customers
     */
    public List<Cocktail> getAvailableCocktails() {
        return cocktailDAO.findByIsVisibleTrueAndIsAvailableTrue();
    }

    /**
     * Get all cocktails (for barmakers)
     */
    public List<Cocktail> getAllCocktails() {
        return cocktailDAO.findAll();
    }

    /**
     * Get cocktails by category
     */
    public List<Cocktail> getCocktailsByCategory(Long categoryId) {
        return cocktailDAO.findByCategoryId(categoryId);
    }

    /**
     * Search cocktails by name
     */
    public List<Cocktail> searchCocktails(String name) {
        return cocktailDAO.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get cocktails on discount
     */
    public List<Cocktail> getDiscountCocktails() {
        return cocktailDAO.findByIsDiscountTrue();
    }

    /**
     * Get cocktail by ID
     */
    public Optional<Cocktail> getCocktailById(Long id) {
        return cocktailDAO.findById(id);
    }

    /**
     * Create new cocktail (barmaker only)
     */
    public Cocktail createCocktail(Cocktail cocktail) {
        return cocktailDAO.save(cocktail);
    }

    /**
     * Update cocktail (barmaker only)
     */
    public Cocktail updateCocktail(Long id, Cocktail cocktailDetails) {
        return cocktailDAO.findById(id)
                .map(cocktail -> {
                    cocktail.setName(cocktailDetails.getName());
                    cocktail.setDescription(cocktailDetails.getDescription());
                    cocktail.setPrice(cocktailDetails.getPrice());
                    cocktail.setIsVisible(cocktailDetails.getIsVisible());
                    cocktail.setIsAvailable(cocktailDetails.getIsAvailable());
                    cocktail.setIsDiscount(cocktailDetails.getIsDiscount());
                    cocktail.setDiscountPrice(cocktailDetails.getDiscountPrice());
                    cocktail.setCategory(cocktailDetails.getCategory());
                    return cocktailDAO.save(cocktail);
                })
                .orElseThrow(() -> new RuntimeException("Cocktail not found with id: " + id));
    }

    /**
     * Toggle cocktail availability
     */
    public Cocktail toggleAvailability(Long id) {
        return cocktailDAO.findById(id)
                .map(cocktail -> {
                    cocktail.setIsAvailable(!cocktail.getIsAvailable());
                    return cocktailDAO.save(cocktail);
                })
                .orElseThrow(() -> new RuntimeException("Cocktail not found with id: " + id));
    }

    /**
     * Toggle cocktail visibility
     */
    public Cocktail toggleVisibility(Long id) {
        return cocktailDAO.findById(id)
                .map(cocktail -> {
                    cocktail.setIsVisible(!cocktail.getIsVisible());
                    return cocktailDAO.save(cocktail);
                })
                .orElseThrow(() -> new RuntimeException("Cocktail not found with id: " + id));
    }

    /**
     * Delete cocktail
     */
    public void deleteCocktail(Long id) {
        cocktailDAO.deleteById(id);
    }
}