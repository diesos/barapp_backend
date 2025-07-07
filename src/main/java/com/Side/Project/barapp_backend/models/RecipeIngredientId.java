package com.Side.Project.barapp_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecipeIngredientId implements Serializable {

  @Column(name = "recipe_id")
  private Long recipeId;

  @Column(name = "ingredient_id")
  private Long ingredientId;

  // Constructors
  public RecipeIngredientId() {}

  public RecipeIngredientId(Long recipeId, Long ingredientId) {
    this.recipeId = recipeId;
    this.ingredientId = ingredientId;
  }

  // Getters and Setters
  public Long getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(Long recipeId) {
    this.recipeId = recipeId;
  }

  public Long getIngredientId() {
    return ingredientId;
  }

  public void setIngredientId(Long ingredientId) {
    this.ingredientId = ingredientId;
  }

  // equals and hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RecipeIngredientId that = (RecipeIngredientId) o;
    return Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(ingredientId, that.ingredientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipeId, ingredientId);
  }
}