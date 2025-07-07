package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Represents the size of a cocktail with its associated price.
 * Each cocktail can have multiple sizes (e.g., Small, Medium, Large).
 */

@Entity
@Table(name = "cocktail_size")
public class CocktailSize {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "size", nullable = false, length = 2) // "S", "M", "L"
  private String size;

  @Column(name = "price", nullable = false)
  private Integer price;

  @ManyToOne
  @JoinColumn(name = "cocktail_id")
  private Cocktail cocktail;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

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

  @JsonIgnore
  public Cocktail getCocktail() {
    return cocktail;
  };

  public void setCocktail(Cocktail cocktail) {
    this.cocktail = cocktail;
  }
}
