package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
public class Recipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "description", length = 255)
  private String description;

  @JsonIgnore
  @ManyToOne(optional = false)
  @JoinColumn(name = "cocktail_id", nullable = false)
  private Cocktail cocktail;

  @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

  public Long getCocktailId() {
    return cocktail != null ? cocktail.getId() : null;
  }

  public String getCocktailName() {
    return cocktail != null ? cocktail.getName() : null;
  }

  // Constructors
  public Recipe() {
  }

  public Recipe(String name, String description, Cocktail cocktail) {
    this.name = name;
    this.description = description;
    this.cocktail = cocktail;
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

  public Cocktail getCocktail() {
    return cocktail;
  }

  public void setCocktail(Cocktail cocktail) {
    this.cocktail = cocktail;
  }

  public List<RecipeIngredient> getRecipeIngredients() {
    return recipeIngredients;
  }

  public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
    this.recipeIngredients = recipeIngredients;
  }
}
