package com.Side.Project.barapp_backend.dao;

import com.Side.Project.barapp_backend.models.CocktailSize;
import org.springframework.data.repository.ListCrudRepository; // ou JpaRepository

public interface CocktailSizeDAO extends ListCrudRepository<CocktailSize, Long> {
}
