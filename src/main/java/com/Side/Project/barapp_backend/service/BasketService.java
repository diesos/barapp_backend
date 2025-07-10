package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.*;
import com.Side.Project.barapp_backend.dao.BasketDAO;
import com.Side.Project.barapp_backend.dao.BasketLineDAO;
import com.Side.Project.barapp_backend.dao.CocktailDAO;
import com.Side.Project.barapp_backend.dao.CocktailSizeDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BasketService {

    private final BasketDAO basketDAO;
    private final BasketLineDAO basketLineDAO;
    private final CocktailDAO cocktailDAO;
    private final CocktailSizeDAO cocktailSizeDAO;

    public BasketService(BasketDAO basketDAO, BasketLineDAO basketLineDAO, CocktailDAO cocktailDAO,
            CocktailSizeDAO cocktailSizeDAO) {
        this.basketDAO = basketDAO;
        this.basketLineDAO = basketLineDAO;
        this.cocktailDAO = cocktailDAO;
        this.cocktailSizeDAO = cocktailSizeDAO;
    }

    /**
     * Get or create current basket for user
     */
    public Basket getCurrentBasket(User user) {
        Optional<Basket> existingBasket = basketDAO.findByUser(user);

        if (existingBasket.isPresent()) {
            return existingBasket.get();
        } else {
            Basket newBasket = new Basket(user);
            return basketDAO.save(newBasket);
        }
    }

    /**
     * Add cocktail to basket (cocktail + taille)
     */
    @Transactional
    public BasketLine addCocktailToBasket(User user, Long cocktailId, Long cocktailSizeId, Integer quantity) {
        Cocktail cocktail = cocktailDAO.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));

        CocktailSize cocktailSize = cocktailSizeDAO.findById(cocktailSizeId)
                .orElseThrow(() -> new RuntimeException("Cocktail size not found"));

        if (!cocktail.getIsAvailable() || !cocktail.getIsVisible()) {
            throw new RuntimeException("Cocktail is not available");
        }

        Basket basket = getCurrentBasket(user);

        // Check if cocktail+taille already in basket
        Optional<BasketLine> existingLine = basketLineDAO.findByBasketAndCocktailAndCocktailSize(basket, cocktail,
                cocktailSize);

        if (existingLine.isPresent()) {
            // Update quantity
            BasketLine line = existingLine.get();
            line.setQuantity(line.getQuantity() + quantity);
            return basketLineDAO.save(line);
        } else {
            // Create new line
            BasketLine newLine = new BasketLine(
                    cocktail,
                    cocktailSize,
                    cocktailSize.getPrice(),
                    quantity,
                    basket);
            return basketLineDAO.save(newLine);
        }
    }

    /**
     * Update cocktail quantity in basket (cocktail + taille)
     */
    @Transactional
    public BasketLine updateCocktailQuantity(User user, Long cocktailId, Long cocktailSizeId, Integer newQuantity) {
        if (newQuantity <= 0) {
            removeCocktailFromBasket(user, cocktailId, cocktailSizeId);
            return null;
        }

        Basket basket = getCurrentBasket(user);
        Cocktail cocktail = cocktailDAO.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));
        CocktailSize cocktailSize = cocktailSizeDAO.findById(cocktailSizeId)
                .orElseThrow(() -> new RuntimeException("Cocktail size not found"));

        BasketLine line = basketLineDAO.findByBasketAndCocktailAndCocktailSize(basket, cocktail, cocktailSize)
                .orElseThrow(() -> new RuntimeException("Cocktail/size not in basket"));

        line.setQuantity(newQuantity);
        return basketLineDAO.save(line);
    }

    /**
     * Remove cocktail from basket (cocktail + taille)
     */
    @Transactional
    public void removeCocktailFromBasket(User user, Long cocktailId, Long cocktailSizeId) {
        Basket basket = getCurrentBasket(user);
        Cocktail cocktail = cocktailDAO.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));
        CocktailSize cocktailSize = cocktailSizeDAO.findById(cocktailSizeId)
                .orElseThrow(() -> new RuntimeException("Cocktail size not found"));

        basketLineDAO.findByBasketAndCocktailAndCocktailSize(basket, cocktail, cocktailSize)
                .ifPresent(basketLineDAO::delete);
    }

    /**
     * Clear basket
     */
    @Transactional
    public void clearBasket(User user) {
        Basket basket = getCurrentBasket(user);
        basketLineDAO.deleteByBasket(basket);
    }

    /**
     * Get basket total
     */
    public Integer getBasketTotal(User user) {
        Basket basket = getCurrentBasket(user);
        return basket.getTotalAmount();
    }
}
