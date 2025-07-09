package com.Side.Project.barapp_backend.controller.category;

import com.Side.Project.barapp_backend.api.models.CategoryRequest;
import com.Side.Project.barapp_backend.api.models.CategoryResponse;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/**
	 * Get all main categories (public endpoint)
	 */
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> getAllMainCategories() {
		List<CategoryResponse> categoryResponses = categoryService.getAllMainCategories()
				.stream().map(CategoryResponse::new).toList();
		return ResponseEntity.ok(categoryResponses);
	}

	/**
	 * Get category by ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
		return categoryService.getCategoryById(id)
				.map(CategoryResponse::new)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Get subcategories by parent category ID
	 */
	@GetMapping("/parent/{parentId}")
	public ResponseEntity<List<CategoryResponse>> getSubcategoriesByParentId(@PathVariable Long parentId) {
		List<CategoryResponse> subcategories = categoryService.getSubcategoriesByParentId(parentId)
				.stream().map(CategoryResponse::new).toList();
		return ResponseEntity.ok(subcategories);
	}

	/**
	 * Search categories by name
	 */
	@GetMapping("/search")
	public ResponseEntity<List<CategoryResponse>> searchCategoriesByName(@RequestParam String name) {
		List<CategoryResponse> categories = categoryService.searchCategoriesByName(name)
				.stream().map(CategoryResponse::new).toList();
		return ResponseEntity.ok(categories);
	}

	/**
	 * Create new category (barmaker or admin only)
	 */
	@PreAuthorize("hasAnyRole('ROLE_BARMAKER', 'ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryResponse> createCategory(
			@RequestBody CategoryRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			CategoryResponse createdCategory = new CategoryResponse(categoryService.createCategory(request));
			return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	/**
	 * Update category (barmaker or admin only)
	 */
	@PreAuthorize("hasAnyRole('ROLE_BARMAKER', 'ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> updateCategory(
			@PathVariable Long id,
			@RequestBody CategoryRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			CategoryResponse updatedCategory = new CategoryResponse(categoryService.updateCategory(id, request));
			return ResponseEntity.ok(updatedCategory);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Delete category (barmaker or admin only)
	 */
	@PreAuthorize("hasAnyRole('ROLE_BARMAKER', 'ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(
			@PathVariable Long id,
			@AuthenticationPrincipal User user) {

		if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		try {
			categoryService.deleteCategory(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
