package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.Order;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.OrderStatus;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface OrderDAO extends ListCrudRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}