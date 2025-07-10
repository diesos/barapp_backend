package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "basket_line")
public class BasketLine {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "cocktail_id", nullable = false)
  private Cocktail cocktail;

  // === AJOUT DE LA TAILLE ===
  @ManyToOne(optional = false)
  @JoinColumn(name = "cocktail_size_id", nullable = false)
  private CocktailSize cocktailSize;

  @Column(name = "unit_price", nullable = false)
  private Integer unitPrice;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "total_sum", nullable = false)
  private Integer totalSum;

  @JsonIgnore
  @ManyToOne(optional = false)
  @JoinColumn(name = "basket_id", nullable = false)
  private Basket basket;

  // Constructors
  public BasketLine() {
  }

  public BasketLine(Cocktail cocktail, CocktailSize cocktailSize, Integer unitPrice, Integer quantity, Basket basket) {
    this.cocktail = cocktail;
    this.cocktailSize = cocktailSize;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.basket = basket;
    this.calculateTotalSum();
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Integer unitPrice) {
    this.unitPrice = unitPrice;
    this.calculateTotalSum();
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
    this.calculateTotalSum();
  }

  public Integer getTotalSum() {
    return totalSum;
  }

  public void setTotalSum(Integer totalSum) {
    this.totalSum = totalSum;
  }

  public Basket getBasket() {
    return basket;
  }

  public void setBasket(Basket basket) {
    this.basket = basket;
  }

  /**
   * Calculate and update total sum
   */
  private void calculateTotalSum() {
    if (unitPrice != null && quantity != null) {
      this.totalSum = unitPrice * quantity;
    }
  }

  /**
   * Update quantity and recalculate total
   */
  public void updateQuantity(Integer newQuantity) {
    this.quantity = newQuantity;
    this.calculateTotalSum();
  }

  @PrePersist
  @PreUpdate
  protected void onSave() {
    this.calculateTotalSum();
  }
}
