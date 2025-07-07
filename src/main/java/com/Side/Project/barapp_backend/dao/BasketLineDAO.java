package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.BasketLine;
import com.Side.Project.barapp_backend.models.Basket;
import com.Side.Project.barapp_backend.models.Cocktail;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface BasketLineDAO extends ListCrudRepository<BasketLine, Long> {
    List<BasketLine> findByBasket(Basket basket);
    Optional<BasketLine> findByBasketAndCocktail(Basket basket, Cocktail cocktail);
    void deleteByBasket(Basket basket);
}