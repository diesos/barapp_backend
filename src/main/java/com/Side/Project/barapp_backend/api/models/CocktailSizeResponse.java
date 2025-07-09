package com.Side.Project.barapp_backend.api.models;

public class CocktailSizeResponse {
	private Long id;
	private String size;
	private Integer price;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CocktailSizeResponse(Long id, String size, Integer price) {
		this.id = id;
		this.size = size;
		this.price = price;
	}
}
