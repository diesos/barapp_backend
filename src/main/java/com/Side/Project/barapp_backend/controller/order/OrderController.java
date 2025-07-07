package com.Side.Project.barapp_backend.controller.order;

import com.Side.Project.barapp_backend.models.Order;
import com.Side.Project.barapp_backend.models.OrderStatus;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create order from current basket
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@AuthenticationPrincipal User user) {
        try {
            Order order = orderService.createOrder(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get current user's orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        return orderService.getOrderById(id)
                .map(order -> {
                    // Check if user owns the order or is a barmaker
                    if (order.getUser().getId().equals(user.getId()) ||
                            user.getRole() == UserRole.ROLE_BARMAKER ||
                            user.getRole() == UserRole.ROLE_ADMIN) {
                        return ResponseEntity.ok(order);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Order>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get order details with basket lines
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Order> getOrderWithDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        try {
            Order order = orderService.getOrderWithDetails(id);

            // Check permissions
            if (order.getUser().getId().equals(user.getId()) ||
                    user.getRole() == UserRole.ROLE_BARMAKER ||
                    user.getRole() == UserRole.ROLE_ADMIN) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cancel order (customer only, and only if not in progress)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Only the order owner can cancel
            if (!order.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            orderService.cancelOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // BARMAKER ENDPOINTS

    /**
     * Get all pending orders (barmaker only)
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Order>> getPendingOrders(@AuthenticationPrincipal User user) {
        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Order> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get orders in progress (barmaker only)
     */
    @GetMapping("/in-progress")
    public ResponseEntity<List<Order>> getOrdersInProgress(@AuthenticationPrincipal User user) {
        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Order> orders = orderService.getOrdersInProgress();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get orders by status (barmaker only)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Start order preparation (barmaker only)
     */
    @PatchMapping("/{id}/start")
    public ResponseEntity<Order> startOrderPreparation(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Order updatedOrder = orderService.startOrderPreparation(id);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Complete order (barmaker only)
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Order> completeOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Order updatedOrder = orderService.completeOrder(id);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update order status (barmaker only)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}