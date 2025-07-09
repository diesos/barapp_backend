package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.api.models.CategoryRequest;
import com.Side.Project.barapp_backend.dao.CategoryDAO;
import com.Side.Project.barapp_backend.models.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	private final CategoryDAO categoryDAO;

	public CategoryService(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	@Transactional(readOnly = true)
	public List<Category> getAllMainCategories() {
		return categoryDAO.findByParentIsNull();
	}

	@Transactional(readOnly = true)
	public Optional<Category> getCategoryById(Long id) {
		return categoryDAO.findById(id);
	}

	@Transactional(readOnly = true)
	public List<Category> getSubcategoriesByParentId(Long parentId) {
		return categoryDAO.findByParentId(parentId);
	}

	@Transactional(readOnly = true)
	public List<Category> searchCategoriesByName(String name) {
		return categoryDAO.findByNameContainingIgnoreCase(name);
	}

	@Transactional
	public Category createCategory(CategoryRequest categoryRequest) {
		// S'il y a un parent envoyé dans la requête, on va le fetch en BDD
		Category category = new Category();
		category.setName(categoryRequest.getName());
		if (categoryRequest.getParentId() != null) {
			Category parent = categoryDAO.findById(categoryRequest.getParentId())
					.orElseThrow(() -> new RuntimeException("Parent not found"));
			category.setParent(parent);
		}
		return categoryDAO.save(category);
	}

	@Transactional
	public Category updateCategory(Long id, CategoryRequest request) {
		return categoryDAO.findById(id)
				.map(category -> {
					category.setName(request.getName());
					if (request.getParentId() != null) {
						Category parent = categoryDAO.findById(request.getParentId())
								.orElseThrow(() -> new RuntimeException("Parent not found"));
						category.setParent(parent);
					} else {
						category.setParent(null);
					}
					// Update other fields as necessary
					return categoryDAO.save(category);
				}).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
	}

	@Transactional
	public void deleteCategory(Long id) {
		if (!categoryDAO.existsById(id)) {
			throw new RuntimeException("Category not found with id " + id);
		}
		categoryDAO.deleteById(id);
	}
}
