package com.Side.Project.barapp_backend.controller.ingredient;

import com.Side.Project.barapp_backend.models.Ingredient;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Get all ingredients (barmaker/admin only)
     */
    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients(@AuthenticationPrincipal User user) {
        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Get available ingredients (public)
     */
    @GetMapping("/available")
    public ResponseEntity<List<Ingredient>> getAvailableIngredients() {
        List<Ingredient> ingredients = ingredientService.getAvailableIngredients();
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Get ingredient by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id)
                .map(ingredient -> ResponseEntity.ok(ingredient))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search ingredients by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Ingredient>> searchIngredients(@RequestParam String name) {
        List<Ingredient> ingredients = ingredientService.searchIngredients(name);
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Create new ingredient (barmaker/admin only)
     */
    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(
            @RequestBody Ingredient ingredient,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Ingredient createdIngredient = ingredientService.createIngredient(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIngredient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update ingredient (barmaker/admin only)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable Long id,
            @RequestBody Ingredient ingredientDetails,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Ingredient updatedIngredient = ingredientService.updateIngredient(id, ingredientDetails);
            return ResponseEntity.ok(updatedIngredient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Toggle ingredient availability (barmaker/admin only)
     */
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<Ingredient> toggleAvailability(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Ingredient updatedIngredient = ingredientService.toggleAvailability(id);
            return ResponseEntity.ok(updatedIngredient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete ingredient (barmaker/admin only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            ingredientService.deleteIngredient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
