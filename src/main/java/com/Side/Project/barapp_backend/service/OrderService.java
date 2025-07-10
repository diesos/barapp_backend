package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.*;
import com.Side.Project.barapp_backend.dao.OrderDAO;
import com.Side.Project.barapp_backend.dao.BasketDAO;
import com.Side.Project.barapp_backend.dao.OrderLineDAO;
import com.Side.Project.barapp_backend.dao.OrderLineDAO;
import com.Side.Project.barapp_backend.dao.BasketLineDAO;

import com.Side.Project.barapp_backend.api.models.OrderLineResponse;
import com.Side.Project.barapp_backend.api.models.OrderResponse;
import com.Side.Project.barapp_backend.api.models.UserResponse;
import com.Side.Project.barapp_backend.api.models.CocktailResponse;
import com.Side.Project.barapp_backend.api.models.CocktailSizeResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderDAO orderDAO;
    private final BasketDAO basketDAO;
    private final BasketService basketService;
    private final OrderLineDAO orderLineDAO;

    public OrderService(OrderDAO orderDAO, BasketDAO basketDAO, BasketService basketService,
            OrderLineDAO orderLineDAO) {
        this.orderDAO = orderDAO;
        this.basketDAO = basketDAO;
        this.basketService = basketService;
        this.orderLineDAO = orderLineDAO;
    }

    /**
     * Retrieves an Order entity by its ID. Used internally for operations requiring
     * the full entity.
     *
     * @param id The ID of the order.
     * @return An Optional containing the Order entity, or empty if not found.
     */
    @Transactional(readOnly = true) // Lecture seule, pas de modifications de données
    public Optional<Order> getOrderEntityById(Long id) {
        return orderDAO.findById(id);
    }

    /**
     * Create order from current basket (conversion panier -> commande)
     */
    @Transactional
    public Order createOrder(User user) {
        Basket currentBasket = basketService.getCurrentBasket(user);

        if (currentBasket.getBasketLines().isEmpty()) {
            throw new RuntimeException("Cannot create order from empty basket");
        }

        // Créer la commande
        Order order = new Order(user, currentBasket, OrderStatus.ORDERED);
        order = orderDAO.save(order);

        // Pour chaque ligne du panier, créer une ligne de commande
        List<BasketLine> linesToProcess = new ArrayList<>(currentBasket.getBasketLines());
        for (BasketLine basketLine : linesToProcess) {
            OrderLine orderLine = new OrderLine(
                    order,
                    basketLine.getCocktail(),
                    basketLine.getCocktailSize(),
                    basketLine.getQuantity(),
                    basketLine.getUnitPrice());
            orderLineDAO.save(orderLine);
            order.getOrderLines().add(orderLine); // Optionnel, pour mise à jour mémoire
        }
        currentBasket.getBasketLines().clear(); // Vider le panier après conversion
        if (!currentBasket.getBasketLines().isEmpty()) {
            throw new RuntimeException("Basket lines were not properly cleared");
        }
        basketDAO.save(currentBasket);

        return order;
    }

    /**
     * Get user's orders
     */
    public List<OrderResponse> getUserOrders(User user) {
        return orderDAO.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get orders by status (for barmakers)
     */
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderDAO.findByStatusOrderByCreatedAtAsc(status).stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all pending orders (for barmakers)
     */
    public List<OrderResponse> getPendingOrders() {
        return orderDAO.findByStatusOrderByCreatedAtAsc(OrderStatus.ORDERED).stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get orders in progress (for barmakers)
     */
    public List<OrderResponse> getOrdersInProgress() {
        return orderDAO.findByStatusOrderByCreatedAtAsc(OrderStatus.IN_PROGRESS).stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get order by ID
     */
    public Optional<OrderResponse> getOrderById(Long id) {
        return orderDAO.findById(id)
                .map(this::mapOrderToOrderResponse); // Mapper directement ici
    }

    /**
     * Update order status (barmaker only)
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        return orderDAO.findById(orderId)
                .map(order -> {
                    order.setStatus(newStatus);
                    return orderDAO.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    /**
     * Start order preparation (barmaker only)
     */
    @Transactional
    public Order startOrderPreparation(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.IN_PROGRESS);
    }

    /**
     * Complete order (barmaker only)
     */
    @Transactional
    public Order completeOrder(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    /**
     * Get order details with basket lines
     */
    public Order getOrderWithDetails(Long orderId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.getOrderLines().size(); // Force load (si LAZY)
        return order;
    }

    /**
     * Cancel order (only if not in progress or completed)
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot cancel order that is in progress");
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed order");
        }
        orderDAO.delete(order);
    }

    public OrderResponse mapOrderToOrderResponse(Order order) {
        // Crucial: Force loading of orderLines collection if it's LAZY-loaded.
        // This ensures all lines are present when we iterate over them.
        if (order.getOrderLines() != null) {
            order.getOrderLines().size(); // Forces initialization of the collection
        }

        // Map the User entity to UserResponse DTO
        UserResponse userResponse = new UserResponse(
                order.getUser().getId(),
                order.getUser().getEmail(),
                order.getUser().getRole());

        // Map each OrderLine entity to an OrderLineResponse DTO
        List<OrderLineResponse> orderLineResponses = order.getOrderLines().stream()
                .map(orderLine -> {
                    // Map the Cocktail entity to CocktailResponse DTO
                    CocktailResponse cocktailResponse = new CocktailResponse(
                            orderLine.getCocktail().getId(),
                            orderLine.getCocktail().getName(), // Correct: Cocktail name from Cocktail entity
                            orderLine.getCocktail().getImageUrl() // Correct: Image URL from Cocktail entity
                    );

                    // Map the CocktailSize entity to CocktailSizeResponse DTO
                    // Using getSize() for the size string (e.g., "S", "M", "L")
                    // and getPrice() for the price specific to that size.
                    CocktailSizeResponse cocktailSizeResponse = new CocktailSizeResponse(
                            orderLine.getCocktailSize().getId(),
                            orderLine.getCocktailSize().getSize(), // Correct: Size string from CocktailSize entity
                            orderLine.getCocktailSize().getPrice() // Correct: Price from CocktailSize entity
                    );

                    // Create the OrderLineResponse, embedding the nested DTOs
                    return new OrderLineResponse(
                            orderLine.getId(),
                            cocktailResponse, // Pass the mapped Cocktail DTO
                            cocktailSizeResponse, // Pass the mapped CocktailSize DTO
                            orderLine.getQuantity(),
                            orderLine.getUnitPrice() // Unit price as recorded in the order line
                    );
                })
                .collect(Collectors.toList());

        // Construct the final OrderResponse DTO
        return new OrderResponse(
                order.getId(),
                userResponse,
                order.getStatus(),
                order.getCreatedAt(),
                order.getTotalAmount(), // These methods are on the Order entity and calculate from its orderLines
                order.getTotalItems(),
                orderLineResponses);
    }
}
