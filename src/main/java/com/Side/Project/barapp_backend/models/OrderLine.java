package com.Side.Project.barapp_backend.models;

import jakarta.persistence.*;
import com.Side.Project.barapp_backend.models.Cocktail;
import com.Side.Project.barapp_backend.models.Order;
import com.Side.Project.barapp_backend.models.CocktailSize;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_line")
public class OrderLine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(optional = false)
	@JoinColumn(name = "cocktail_id")
	private Cocktail cocktail;

	@ManyToOne(optional = false)
	@JoinColumn(name = "size_id")
	private CocktailSize cocktailSize;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Integer unitPrice; // En centimes, ou décimaux selon ton système

	public OrderLine() {
	}

	public OrderLine(Order order, Cocktail cocktail, CocktailSize cocktailSize, Integer quantity, Integer unitPrice) {
		this.order = order;
		this.cocktail = cocktail;
		this.cocktailSize = cocktailSize;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Cocktail getCocktail() {
		return cocktail;
	}

	public void setCocktail(Cocktail cocktail) {
		this.cocktail = cocktail;
	}

	public CocktailSize getCocktailSize() {
		return cocktailSize;
	}

	public void setCocktailSize(CocktailSize cocktailSize) {
		this.cocktailSize = cocktailSize;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getTotalPrice() {
		return this.unitPrice * this.quantity; // Total en centimes
	}

	public String getCocktailName() {
		return cocktail != null ? cocktail.getName() : "Unknown Cocktail";
	}

}
