package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.OrderLine;
import com.Side.Project.barapp_backend.models.Order;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface OrderLineDAO extends ListCrudRepository<OrderLine, Long> {
	// Obtenir toutes les lignes d'une commande donnée
	List<OrderLine> findByOrder(Order order);

	// Si besoin : trouver toutes les lignes pour un cocktail précis
	List<OrderLine> findByCocktailId(Long cocktailId);
}
