package com.Side.Project.barapp_backend.api.models;

import java.util.List;
import com.Side.Project.barapp_backend.models.Category;

public class CocktailResponse {
	private Long id;
	private String name;
	private String description;
	private Boolean isVisible;
	private Boolean isAvailable;
	private Boolean isDiscount;
	private Integer discountPrice;
	private Category category;
	private List<CocktailSizeResponse> sizes;
	private String imageUrl;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CocktailSizeResponse> getSizes() {
		return sizes;
	}

	public void setSizes(List<CocktailSizeResponse> sizes) {
		this.sizes = sizes;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Boolean getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(Boolean isDiscount) {
		this.isDiscount = isDiscount;
	}

	public Integer getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CocktailResponse() {

	}

	public CocktailResponse(Long id, String name, String description, Boolean isVisible, Boolean isAvailable,
			Boolean isDiscount, Integer discountPrice, Category category, List<CocktailSizeResponse> sizes,
			String imageUrl) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isVisible = isVisible;
		this.isAvailable = isAvailable;
		this.isDiscount = isDiscount;
		this.discountPrice = discountPrice;
		this.category = category;
		this.sizes = sizes;
		this.imageUrl = imageUrl;
	}
}
