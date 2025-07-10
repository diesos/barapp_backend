package com.Side.Project.barapp_backend.controller.order;

import com.Side.Project.barapp_backend.models.Order;
import com.Side.Project.barapp_backend.models.OrderStatus;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.api.models.OrderResponse;
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
     * Create order from current basket.
     * Returns the created order as an OrderResponse DTO.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal User user) {
        try {
            // The service returns the Order entity.
            // We then explicitly map it to OrderResponse before returning.
            Order createdOrder = orderService.createOrder(user);
            OrderResponse response = orderService.mapOrderToOrderResponse(createdOrder); // Use mapping from service
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Provide specific error messages if possible, e.g., if "Cannot create order
            // from empty basket"
            if (e.getMessage() != null && e.getMessage().contains("empty basket")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a custom error DTO
            }
            // Generic bad request for other runtime exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Get current user's orders.
     * Returns a list of OrderResponse DTOs.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal User user) {
        // The service is already designed to return List<OrderResponse>
        List<OrderResponse> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID for the current user or privileged roles.
     * Returns an Optional<OrderResponse> DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        return orderService.getOrderById(id) // Service returns Optional<OrderResponse>
                .map(orderResponse -> {
                    // Check if the authenticated user owns the order or has a privileged role.
                    // We check against the user ID in the DTO's user field.
                    if (orderResponse.getUser().getId().equals(user.getId()) ||
                            user.getRole() == UserRole.ROLE_BARMAKER ||
                            user.getRole() == UserRole.ROLE_ADMIN) {
                        return ResponseEntity.ok(orderResponse);
                    } else {
                        // Forbidden if user doesn't have permissions
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<OrderResponse>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build()); // Not found if order doesn't exist
    }

    /**
     * Get order details with order lines.
     * This endpoint is now redundant if getOrderById already returns full details
     * via DTO.
     * It can simply delegate to getOrderById.
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<OrderResponse> getOrderWithDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        // Reusing the getOrderById logic as it now provides all necessary details
        return getOrderById(id, user);
    }

    /**
     * Cancel order (customer only, and only if not in progress).
     * Returns 204 No Content on success, or a BAD_REQUEST/FORBIDDEN status.
     * Return type changed to Void as per 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder( // Changed return type to Void
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        try {
            // Fetch the actual Order entity to perform checks before cancellation
            // The service method should ideally handle permission checks or throw specific
            // exceptions
            Order orderToCancel = orderService.getOrderEntityById(id) // Need a new method in service to get the entity
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Only the order owner can cancel
            if (!orderToCancel.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            orderService.cancelOrder(id);
            return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace();
            // Handle specific cases like "Order cannot be cancelled in current status"
            if (e.getMessage() != null && e.getMessage().contains("cannot be cancelled")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get all pending orders (barmaker/admin only).
     * Returns a list of OrderResponse DTOs.
     */
    @GetMapping("/pending")

    public ResponseEntity<List<OrderResponse>> getPendingOrders(@AuthenticationPrincipal User user) {
        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<OrderResponse> orders = orderService.getPendingOrders(); // Service returns DTOs
        return ResponseEntity.ok(orders);
    }

    /**
     * Get orders in progress (barmaker/admin only).
     * Returns a list of OrderResponse DTOs.
     */
    @GetMapping("/in-progress")
    public ResponseEntity<List<OrderResponse>> getOrdersInProgress(@AuthenticationPrincipal User user) {
        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<OrderResponse> orders = orderService.getOrdersInProgress(); // Service returns DTOs
        return ResponseEntity.ok(orders);
    }

    /**
     * Get orders by status (barmaker/admin only).
     * Returns a list of OrderResponse DTOs.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<OrderResponse> orders = orderService.getOrdersByStatus(status); // Service returns DTOs
        return ResponseEntity.ok(orders);
    }

    /**
     * Start order preparation (barmaker/admin only).
     * Returns the updated order as an OrderResponse DTO.
     */
    @PatchMapping("/{id}/start")
    public ResponseEntity<OrderResponse> startOrderPreparation( // Changed return type to OrderResponse
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Order updatedOrder = orderService.startOrderPreparation(id); // Service returns Order entity
            OrderResponse response = orderService.mapOrderToOrderResponse(updatedOrder); // Map to DTO
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.notFound().build(); // Or a more specific error
        }
    }

    /**
     * Complete order (barmaker/admin only).
     * Returns the updated order as an OrderResponse DTO.
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<OrderResponse> completeOrder( // Changed return type to OrderResponse
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Order updatedOrder = orderService.completeOrder(id); // Service returns Order entity
            OrderResponse response = orderService.mapOrderToOrderResponse(updatedOrder); // Map to DTO
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.notFound().build(); // Or a more specific error
        }
    }

    /**
     * Update order status (barmaker/admin only).
     * Returns the updated order as an OrderResponse DTO.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus( // Changed return type to OrderResponse
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal User user) {

        if (user.getRole() != UserRole.ROLE_BARMAKER && user.getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status); // Service returns Order entity
            OrderResponse response = orderService.mapOrderToOrderResponse(updatedOrder); // Map to DTO
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.notFound().build(); // Or a more specific error
        }
    }
}
