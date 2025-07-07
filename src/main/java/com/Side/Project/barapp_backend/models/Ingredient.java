package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredient")
public class Ingredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "is_available", nullable = false)
  private Boolean isAvailable = true;

  @JsonIgnore
  @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

  // Constructors
  public Ingredient() {}

  public Ingredient(String name, Integer price) {
    this.name = name;
    this.price = price;
  }

  public Ingredient(String name, Integer price, Boolean isAvailable) {
    this.name = name;
    this.price = price;
    this.isAvailable = isAvailable;
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

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Boolean getIsAvailable() {
    return isAvailable;
  }

  public void setIsAvailable(Boolean isAvailable) {
    this.isAvailable = isAvailable;
  }

  public List<RecipeIngredient> getRecipeIngredients() {
    return recipeIngredients;
  }

  public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
    this.recipeIngredients = recipeIngredients;
  }
}