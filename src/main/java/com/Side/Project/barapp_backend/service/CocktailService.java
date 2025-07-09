package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.Cocktail;
import com.Side.Project.barapp_backend.models.CocktailSize;
import com.Side.Project.barapp_backend.api.models.CocktailRequest;
import com.Side.Project.barapp_backend.api.models.CocktailSizeRequest;
import com.Side.Project.barapp_backend.dao.CocktailDAO;
import com.Side.Project.barapp_backend.dao.CategoryDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CocktailService {

    private final CocktailDAO cocktailDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    public CocktailService(CocktailDAO cocktailDAO) {
        this.cocktailDAO = cocktailDAO;
    }

    /**
     * Get all available cocktails for customers
     */
    public List<Cocktail> getAvailableCocktails() {
        return cocktailDAO.findByIsVisibleTrueAndIsAvailableTrue();
    }

    public List<Cocktail> getVisibleCocktails() {
        return cocktailDAO.findByIsVisibleTrue();
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
    public Cocktail createCocktail(CocktailRequest cocktailRequest) {
        Cocktail cocktail = new Cocktail();
        cocktail.setName(cocktailRequest.getName());
        cocktail.setDescription(cocktailRequest.getDescription());
        cocktail.setIsVisible(cocktailRequest.getIsVisible());
        cocktail.setIsAvailable(cocktailRequest.getIsAvailable());
        cocktail.setIsDiscount(cocktailRequest.getIsDiscount());
        cocktail.setDiscountPrice(cocktailRequest.getDiscountPrice());
        cocktail.setCategory(categoryDAO.findById(cocktailRequest.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found with id: " + cocktailRequest.getCategoryId())));
        cocktail.setImageUrl(cocktailRequest.getImageUrl());

        // On initialise les sizes et on les ajoute à la liste du cocktail
        List<CocktailSize> sizes = new ArrayList<>();
        if (cocktailRequest.getSizes() != null) {
            for (CocktailSizeRequest sizeRequest : cocktailRequest.getSizes()) {
                CocktailSize size = new CocktailSize();
                size.setSize(sizeRequest.getSize());
                size.setPrice(sizeRequest.getPrice());
                size.setCocktail(cocktail); // très important : lien parent
                sizes.add(size);
            }
        }
        cocktail.setSizes(sizes); // lie la liste à ton cocktail

        return cocktailDAO.save(cocktail);
    }

    /**
     * Update cocktail (barmaker only)
     */
    public Cocktail updateCocktail(Long id, CocktailRequest cocktailRequest) {
        return cocktailDAO.findById(id)
                .map(cocktail -> {
                    cocktail.setName(cocktailRequest.getName());
                    cocktail.setDescription(cocktailRequest.getDescription());
                    cocktail.setIsVisible(cocktailRequest.getIsVisible());
                    cocktail.setIsAvailable(cocktailRequest.getIsAvailable());
                    cocktail.setIsDiscount(cocktailRequest.getIsDiscount());
                    cocktail.setCategory(
                            categoryDAO.findById(cocktailRequest.getCategoryId())
                                    .orElseThrow(() -> new RuntimeException(
                                            "Category not found with id: " + cocktailRequest.getCategoryId())));
                    cocktail.setDiscountPrice(cocktailRequest.getDiscountPrice());
                    cocktail.setImageUrl(cocktailRequest.getImageUrl());

                    List<CocktailSize> sizes = new ArrayList<>();
                    if (cocktailRequest.getSizes() != null) {
                        for (CocktailSizeRequest sizeRequest : cocktailRequest.getSizes()) {
                            CocktailSize size = new CocktailSize();
                            size.setSize(sizeRequest.getSize());
                            size.setPrice(sizeRequest.getPrice());
                            size.setCocktail(cocktail); // très important !
                            sizes.add(size);
                        }
                    }
                    cocktail.getSizes().clear(); // retire les anciennes (grâce à orphanRemoval)
                    cocktail.getSizes().addAll(sizes);

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
                    if (cocktail.getIsAvailable() == null) {
                        cocktail.setIsAvailable(false);
                    }
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
