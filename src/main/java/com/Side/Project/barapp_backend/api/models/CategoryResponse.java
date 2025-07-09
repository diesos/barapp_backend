package com.Side.Project.barapp_backend.api.models;

import java.util.List;
import com.Side.Project.barapp_backend.models.Category;

public class CategoryResponse {

	private Long id;
	private String name;
	private Long parentId;
	private List<CategoryResponse> subcategories;
	private int childCount;

	public CategoryResponse(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		this.parentId = category.getParent() != null ? category.getParent().getId() : null;
		this.subcategories = category.getSubcategories() != null
				? category.getSubcategories().stream().map(CategoryResponse::new).toList()
				: List.of();
		this.childCount = subcategories.size();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<CategoryResponse> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<CategoryResponse> subcategories) {
		this.subcategories = subcategories;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

}
