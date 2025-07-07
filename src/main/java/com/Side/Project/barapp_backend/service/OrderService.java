package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.models.*;
import com.Side.Project.barapp_backend.dao.OrderDAO;
import com.Side.Project.barapp_backend.dao.BasketDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderDAO orderDAO;
    private final BasketDAO basketDAO;
    private final BasketService basketService;

    public OrderService(OrderDAO orderDAO, BasketDAO basketDAO, BasketService basketService) {
        this.orderDAO = orderDAO;
        this.basketDAO = basketDAO;
        this.basketService = basketService;
    }

    /**
     * Create order from current basket
     */
    @Transactional
    public Order createOrder(User user) {
        Basket basket = basketService.getCurrentBasket(user);

        if (basket.getBasketLines().isEmpty()) {
            throw new RuntimeException("Cannot create order from empty basket");
        }

        // Convert basket
        basketService.convertBasketToOrder(user);

        // Create order
        Order order = new Order(user, basket, OrderStatus.ORDERED);
        return orderDAO.save(order);
    }

    /**
     * Get user's orders
     */
    public List<Order> getUserOrders(User user) {
        return orderDAO.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Get orders by status (for barmakers)
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderDAO.findByStatusOrderByCreatedAtAsc(status);
    }

    /**
     * Get all pending orders (for barmakers)
     */
    public List<Order> getPendingOrders() {
        return orderDAO.findByStatusOrderByCreatedAtAsc(OrderStatus.ORDERED);
    }

    /**
     * Get orders in progress (for barmakers)
     */
    public List<Order> getOrdersInProgress() {
        return orderDAO.findByStatusOrderByCreatedAtAsc(OrderStatus.IN_PROGRESS);
    }

    /**
     * Get order by ID
     */
    public Optional<Order> getOrderById(Long id) {
        return orderDAO.findById(id);
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

        // Force loading of basket lines
        order.getBasket().getBasketLines().size();

        return order;
    }

    /**
     * Calculate order total
     */
    public Integer calculateOrderTotal(Long orderId) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return order.getBasket().getTotalAmount();
    }

    /**
     * Cancel order (only if not in progress)
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
}