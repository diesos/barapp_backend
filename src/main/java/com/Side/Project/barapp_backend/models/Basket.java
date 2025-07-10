package com.Side.Project.barapp_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "baskets", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "user_id", "is_archived", "is_converted" })
})
public class Basket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // @Column(name = "is_archived", nullable = false)
  // private Boolean isArchived = false;

  // @Column(name = "is_converted", nullable = false)
  // private Boolean isConverted = false;

  @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC")
  private List<BasketLine> basketLines = new ArrayList<>();

  // @JsonIgnore
  // @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval =
  // true)
  // private List<Order> orders = new ArrayList<>();

  // Constructors
  public Basket() {
  }

  public Basket(User user) {
    this.user = user;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // public Boolean getIsArchived() {
  // return isArchived;
  // }

  // public void setIsArchived(Boolean isArchived) {
  // this.isArchived = isArchived;
  // }

  // public Boolean getIsConverted() {
  // return isConverted;
  // }

  // public void setIsConverted(Boolean isConverted) {
  // this.isConverted = isConverted;
  // }

  public List<BasketLine> getBasketLines() {
    return basketLines;
  }

  public void setBasketLines(List<BasketLine> basketLines) {
    this.basketLines = basketLines;
  }

  // public List<Order> getOrders() {
  // return orders;
  // }

  // public void setOrders(List<Order> orders) {
  // this.orders = orders;
  // }

  /**
   * Calculate total basket amount
   */
  public Integer getTotalAmount() {
    return basketLines.stream()
        .mapToInt(BasketLine::getTotalSum)
        .sum();
  }

  /**
   * Get total number of items in basket
   */
  public Integer getTotalItems() {
    return basketLines.stream()
        .mapToInt(BasketLine::getQuantity)
        .sum();
  }
}
