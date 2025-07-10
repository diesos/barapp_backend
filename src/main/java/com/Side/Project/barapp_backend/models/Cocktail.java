package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cocktail")
public class Cocktail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "price", nullable = true)
  private Integer price;

  @Column(name = "is_visible", nullable = false)
  private Boolean isVisible = true;

  @Column(name = "is_available", nullable = false)
  private Boolean isAvailable = true;

  @Column(name = "is_discount", nullable = true)
  private Boolean isDiscount = false;

  @Column(name = "discount_price")
  private Integer discountPrice;

  @Column(name = "image_url", length = 255)
  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @JsonIgnore
  @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Recipe> recipes = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BasketLine> basketLines = new ArrayList<>();

  @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC")
  private List<CocktailSize> sizes = new ArrayList<>();

  // Constructors
  public Cocktail() {
  }

  public Cocktail(String name, String description, Integer price) {
    this.name = name;
    this.description = description;
    this.price = price;
  }

  // Getters and Setters
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

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
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

  public List<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  public List<BasketLine> getBasketLines() {
    return basketLines;
  }

  public void setBasketLines(List<BasketLine> basketLines) {
    this.basketLines = basketLines;
  }

  public List<CocktailSize> getSizes() {
    return sizes;
  }

  public void setSizes(List<CocktailSize> sizes) {
    this.sizes = sizes;
  }

  /**
   * Get the current price (discount price if available, otherwise regular price)
   */
  public Integer getCurrentPrice() {
    return isDiscount && discountPrice != null ? discountPrice : price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
