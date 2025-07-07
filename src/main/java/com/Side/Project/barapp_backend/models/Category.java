package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Category> subcategories = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Cocktail> cocktails = new ArrayList<>();

  // Constructors
  public Category() {}

  public Category(String name) {
    this.name = name;
  }

  public Category(String name, Category parent) {
    this.name = name;
    this.parent = parent;
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

  public Category getParent() {
    return parent;
  }

  public void setParent(Category parent) {
    this.parent = parent;
  }

  public List<Category> getSubcategories() {
    return subcategories;
  }

  public void setSubcategories(List<Category> subcategories) {
    this.subcategories = subcategories;
  }

  public List<Cocktail> getCocktails() {
    return cocktails;
  }

  public void setCocktails(List<Cocktail> cocktails) {
    this.cocktails = cocktails;
  }
}