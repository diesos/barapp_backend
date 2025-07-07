package com.Side.Project.barapp_backend.controller.basket;

import com.Side.Project.barapp_backend.models.Basket;
import com.Side.Project.barapp_backend.models.BasketLine;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    /**
     * Get current user's basket
     */
    @GetMapping
    public ResponseEntity<Basket> getCurrentBasket(@AuthenticationPrincipal User user) {
        Basket basket = basketService.getCurrentBasket(user);
        return ResponseEntity.ok(basket);
    }

    /**
     * Add cocktail to basket
     */
    @PostMapping("/add")
    public ResponseEntity<BasketLine> addCocktailToBasket(
            @RequestParam Long cocktailId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @AuthenticationPrincipal User user) {

        try {
            BasketLine basketLine = basketService.addCocktailToBasket(user, cocktailId, quantity);
            return ResponseEntity.ok(basketLine);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update cocktail quantity in basket
     */
    @PutMapping("/update")
    public ResponseEntity<BasketLine> updateCocktailQuantity(
            @RequestParam Long cocktailId,
            @RequestParam Integer quantity,
            @AuthenticationPrincipal User user) {

        try {
            BasketLine basketLine = basketService.updateCocktailQuantity(user, cocktailId, quantity);
            if (basketLine == null) {
                return ResponseEntity.noContent().build(); // Item was removed
            }
            return ResponseEntity.ok(basketLine);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Remove cocktail from basket
     */
    @DeleteMapping("/remove/{cocktailId}")
    public ResponseEntity<Void> removeCocktailFromBasket(
            @PathVariable Long cocktailId,
            @AuthenticationPrincipal User user) {

        try {
            basketService.removeCocktailFromBasket(user, cocktailId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Clear entire basket
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearBasket(@AuthenticationPrincipal User user) {
        try {
            basketService.clearBasket(user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get basket total
     */
    @GetMapping("/total")
    public ResponseEntity<Integer> getBasketTotal(@AuthenticationPrincipal User user) {
        Integer total = basketService.getBasketTotal(user);
        return ResponseEntity.ok(total);
    }
}