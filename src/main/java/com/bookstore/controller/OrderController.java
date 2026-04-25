package com.bookstore.controller;

import com.bookstore.dto.OrderRequest;
import com.bookstore.model.Order;
import com.bookstore.model.OrderStatus;
import com.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order API", description = "API for managing orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{email}")
    @Operation(summary = "Get orders by customer email")
    public ResponseEntity<List<Order>> getOrdersByEmail(@PathVariable String email) {
        // BUG: Improper access control - anyone can view orders by email
        log.info("Fetching orders for customer email: {}", email);
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest request) {
        // BUG: No input length validation - description can be huge
        log.info("Creating order for customer: {} ({})",
                request.getCustomerName(), request.getCustomerEmail());
        Order created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        // BUG: Improper access control - no ownership check
        // Any user can cancel/modify any order by knowing its ID
        Order updated = orderService.updateOrderStatus(id, status);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an order")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        // BUG: Anyone can cancel any order - no authorization
        log.warn("Cancelling order with id: {}", id);
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
