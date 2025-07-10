package com.Side.Project.barapp_backend.api.models;

public class OrderLineResponse {
	private Long id;
	// Pas de référence à 'Order' pour briser la boucle !
	private CocktailResponse cocktail; // DTO pour le cocktail
	private CocktailSizeResponse cocktailSize; // DTO pour la taille du cocktail
	private Integer quantity;
	private Integer unitPrice; // Prix unitaire au moment de la commande
	private Integer totalSum; // Prix total pour cette ligne (quantity * unitPrice)

	// Constructeur
	public OrderLineResponse(Long id, CocktailResponse cocktail, CocktailSizeResponse cocktailSize,
			Integer quantity, Integer unitPrice) {
		this.id = id;
		this.cocktail = cocktail;
		this.cocktailSize = cocktailSize;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalSum = quantity * unitPrice;
	}

	// Getters
	public Long getId() {
		return id;
	}

	public CocktailResponse getCocktail() {
		return cocktail;
	}

	public CocktailSizeResponse getCocktailSize() {
		return cocktailSize;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public Integer getTotalSum() {
		return totalSum;
	}

}
