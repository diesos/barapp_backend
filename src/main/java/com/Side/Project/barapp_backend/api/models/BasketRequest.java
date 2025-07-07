package com.Side.Project.barapp_backend.api.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BasketRequest {

    @NotNull
    private Long cocktailId;

    @NotNull
    @Positive
    private Integer quantity;

    public Long getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Long cocktailId) {
        this.cocktailId = cocktailId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}