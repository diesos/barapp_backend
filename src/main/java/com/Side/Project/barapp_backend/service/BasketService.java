package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.*;
import com.Side.Project.barapp_backend.dao.BasketDAO;
import com.Side.Project.barapp_backend.dao.BasketLineDAO;
import com.Side.Project.barapp_backend.dao.CocktailDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BasketService {

    private final BasketDAO basketDAO;
    private final BasketLineDAO basketLineDAO;
    private final CocktailDAO cocktailDAO;

    public BasketService(BasketDAO basketDAO, BasketLineDAO basketLineDAO, CocktailDAO cocktailDAO) {
        this.basketDAO = basketDAO;
        this.basketLineDAO = basketLineDAO;
        this.cocktailDAO = cocktailDAO;
    }

    /**
     * Get or create current basket for user
     */
    public Basket getCurrentBasket(User user) {
        return basketDAO.findByUserAndIsArchivedFalseAndIsConvertedFalse(user)
                .orElseGet(() -> basketDAO.save(new Basket(user)));
    }

    /**
     * Add cocktail to basket
     */
    @Transactional
    public BasketLine addCocktailToBasket(User user, Long cocktailId, Integer quantity) {
        Cocktail cocktail = cocktailDAO.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));

        if (!cocktail.getIsAvailable() || !cocktail.getIsVisible()) {
            throw new RuntimeException("Cocktail is not available");
        }

        Basket basket = getCurrentBasket(user);

        // Check if cocktail already in basket
        Optional<BasketLine> existingLine = basketLineDAO.findByBasketAndCocktail(basket, cocktail);

        if (existingLine.isPresent()) {
            // Update quantity
            BasketLine line = existingLine.get();
            line.setQuantity(line.getQuantity() + quantity);
            return basketLineDAO.save(line);
        } else {
            // Create new line
            BasketLine newLine = new BasketLine(
                    cocktail,
                    cocktail.getCurrentPrice(),
                    quantity,
                    basket
            );
            return basketLineDAO.save(newLine);
        }
    }

    /**
     * Update cocktail quantity in basket
     */
    @Transactional
    public BasketLine updateCocktailQuantity(User user, Long cocktailId, Integer newQuantity) {
        if (newQuantity <= 0) {
            removeCocktailFromBasket(user, cocktailId);
            return null;
        }

        Basket basket = getCurrentBasket(user);
        Cocktail cocktail = cocktailDAO.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));

        BasketLine line = basketLineDAO.findByBasketAndCocktail(basket, cocktail)
                .orElseThrow(() -> new RuntimeException("Cocktail not in basket"));

        line.setQuantity(newQuantity);
        return basketLineDAO.save(line);
    }

    /**
     * Remove cocktail from basket
     */
    @Transactional
    public void removeCocktailFromBasket(User user, Long cocktailId) {
        Basket basket = getCurrentBasket(user);
        Cocktail cocktail = cocktailDAO.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));

        basketLineDAO.findByBasketAndCocktail(basket, cocktail)
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

    /**
     * Convert basket to order
     */
    @Transactional
    public void convertBasketToOrder(User user) {
        Basket basket = getCurrentBasket(user);

        if (basket.getBasketLines().isEmpty()) {
            throw new RuntimeException("Cannot create order from empty basket");
        }

        basket.setIsConverted(true);
        basketDAO.save(basket);
    }

    /**
     * Archive basket
     */
    @Transactional
    public void archiveBasket(User user) {
        Basket basket = getCurrentBasket(user);
        basket.setIsArchived(true);
        basketDAO.save(basket);
    }
}