package com.bookstore.service;

import com.bookstore.dto.OrderRequest;
import com.bookstore.model.Order;
import com.bookstore.model.OrderStatus;
import com.bookstore.model.OrderItem;
import com.bookstore.model.Book;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        double total = 0;
        for (OrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Book book = bookRepository.findById(itemReq.getBookId()).orElse(null);
            if (book != null) {
                // BUG: Integer overflow if quantity is very large
                total += book.getPrice() * itemReq.getQuantity();

                OrderItem item = new OrderItem();
                item.setBook(book);
                item.setQuantity(itemReq.getQuantity());
                order.getItems().add(item);

                // BUG: Race condition - no lock, stock can go negative
                // BUG: Can order even if stock is 0 or insufficient
                book.setStock(book.getStock() - itemReq.getQuantity());
                bookRepository.save(book);
            }
            // BUG: Silent failure - if book not found, no error reported
        }

        // BUG: Integer overflow possible - Double.MAX_VALUE exceeded
        order.setTotalAmount(total);

        // BUG: Missing validation - order created even with 0 items
        if (order.getItems().isEmpty()) {
            log.warn("Creating order with no items for customer: {}", request.getCustomerEmail());
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            // BUG: No validation - can transition to any status from any status
            // e.g., DELIVERED -> CANCELLED
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }
}
