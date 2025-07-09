package com.Side.Project.barapp_backend.controller.cocktail;

import com.Side.Project.barapp_backend.api.models.CocktailResponse;
import com.Side.Project.barapp_backend.api.models.CocktailSizeResponse;
import com.Side.Project.barapp_backend.models.Cocktail;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.service.CocktailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    /**
     * Get all available cocktails (public endpoint)
     */
    @GetMapping
    public ResponseEntity<List<CocktailResponse>> getVisibleCocktails() {
        List<Cocktail> cocktails = cocktailService.getVisibleCocktails(); // à implémenter
        List<CocktailResponse> responses = cocktails.stream()
                .map(cocktail -> new CocktailResponse(
                        cocktail.getId(),
                        cocktail.getName(),
                        cocktail.getDescription(),
                        cocktail.getIsVisible(),
                        cocktail.getIsAvailable(),
                        cocktail.getIsDiscount(),
                        cocktail.getDiscountPrice(),
                        cocktail.getCategory(),
                        cocktail.getSizes().stream()
                                .map(size -> new CocktailSizeResponse(size.getId(), size.getSize(), size.getPrice()))
                                .collect(Collectors.toList()),
                        cocktail.getImageUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all cocktails (barmaker only)
     */
    @PreAuthorize("hasAnyRole('ROLE_BARMAKER', 'ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<CocktailResponse>> getAllCocktails(@AuthenticationPrincipal User user) {
        List<Cocktail> cocktails = cocktailService.getAllCocktails();
        List<CocktailResponse> responses = cocktails.stream().map(cocktail -> {
            CocktailResponse resp = new CocktailResponse(
                    cocktail.getId(),
                    cocktail.getName(),
                    cocktail.getDescription(),
                    cocktail.getIsVisible(),
                    cocktail.getIsAvailable(),
                    cocktail.getIsDiscount(),
                    cocktail.getDiscountPrice(),
                    cocktail.getCategory(),
                    cocktail.getSizes().stream()
                            .map(size -> new CocktailSizeResponse(size.getId(), size.getSize(), size.getPrice()))
                            .collect(Collectors.toList()),
                    cocktail.getImageUrl() // <-- en dernier et ensuite tu fermes la parenthèse !
            );
            return resp;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get cocktail by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cocktail> getCocktailById(@PathVariable Long id) {
        return cocktailService.getCocktailById(id)
                .map(cocktail -> ResponseEntity.ok(cocktail))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get cocktails by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Cocktail>> getCocktailsByCategory(@PathVariable Long categoryId) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByCategory(categoryId);
        return ResponseEntity.ok(cocktails);
    }

    /**
     * Search cocktails by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Cocktail>> searchCocktails(@RequestParam String name) {
        List<Cocktail> cocktails = cocktailService.searchCocktails(name);
        return ResponseEntity.ok(cocktails);
    }

    /**
     * Get cocktails on discount
     */
    @GetMapping("/discounts")
    public ResponseEntity<List<Cocktail>> getDiscountCocktails() {
        List<Cocktail> cocktails = cocktailService.getDiscountCocktails();
        return ResponseEntity.ok(cocktails);
    }

    /**
     * Create new cocktail (barmaker only)
     */
    @PostMapping
    public ResponseEntity<Cocktail> createCocktail(
            @RequestBody Cocktail cocktail,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Cocktail createdCocktail = cocktailService.createCocktail(cocktail);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCocktail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update cocktail (barmaker only)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cocktail> updateCocktail(
            @PathVariable Long id,
            @RequestBody Cocktail cocktailDetails,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Cocktail updatedCocktail = cocktailService.updateCocktail(id, cocktailDetails);
            return ResponseEntity.ok(updatedCocktail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Toggle cocktail availability (barmaker only)
     */
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<Cocktail> toggleAvailability(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Cocktail updatedCocktail = cocktailService.toggleAvailability(id);
            return ResponseEntity.ok(updatedCocktail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Toggle cocktail visibility (barmaker only)
     */
    @PatchMapping("/{id}/toggle-visibility")
    public ResponseEntity<Cocktail> toggleVisibility(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Cocktail updatedCocktail = cocktailService.toggleVisibility(id);
            return ResponseEntity.ok(updatedCocktail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete cocktail (barmaker only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCocktail(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            cocktailService.deleteCocktail(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
