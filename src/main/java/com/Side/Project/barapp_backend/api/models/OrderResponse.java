package com.Side.Project.barapp_backend.api.models; // Ou payload.response

import com.Side.Project.barapp_backend.models.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
	private Long id;
	private UserResponse user; // Utilisez un DTO pour l'utilisateur
	private OrderStatus status;
	private LocalDateTime createdAt;
	private Integer totalAmount; // Ajout du total
	private Integer totalItems; // Ajout du nombre total d'articles
	private List<OrderLineResponse> orderLines; // Utilise le DTO pour les lignes

	// Constructeur complet
	public OrderResponse(Long id, UserResponse user, OrderStatus status, LocalDateTime createdAt,
			Integer totalAmount, Integer totalItems, List<OrderLineResponse> orderLines) {
		this.id = id;
		this.user = user;
		this.status = status;
		this.createdAt = createdAt;
		this.totalAmount = totalAmount;
		this.totalItems = totalItems;
		this.orderLines = orderLines;
	}

	// Getters
	public Long getId() {
		return id;
	}

	public UserResponse getUser() {
		return user;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public List<OrderLineResponse> getOrderLines() {
		return orderLines;
	}

}
