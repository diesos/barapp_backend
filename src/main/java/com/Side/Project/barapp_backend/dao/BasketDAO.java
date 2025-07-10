package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.Basket;
import com.Side.Project.barapp_backend.models.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface BasketDAO extends ListCrudRepository<Basket, Long> {
    Optional<Basket> findByUser(User user);
}
