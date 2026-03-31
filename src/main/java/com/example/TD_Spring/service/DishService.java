package com.example.TD_Spring.service;

import com.example.TD_Spring.entity.Dish;
import com.example.TD_Spring.entity.Ingredient;
import com.example.TD_Spring.exception.DishNotFoundException;
import com.example.TD_Spring.repository.DishRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class DishService {
    
    private final DishRepository dishRepository;
    
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }
    
    public List<Dish> getAllDishes() throws SQLException {
        return dishRepository.findAll();
    }
    
    public Dish getDishById(Integer id) throws SQLException {
        Dish dish = dishRepository.findById(id);
        if (dish == null) {
            throw new DishNotFoundException("Dish.id=" + id + " is not found");
        }
        return dish;
    }
    
    public List<Ingredient> getFilteredIngredients(Integer dishId, String ingredientName, Double ingredientPriceAround) throws SQLException {
        Dish dish = dishRepository.findById(dishId);
        if (dish == null) {
            throw new DishNotFoundException("Dish.id=" + dishId + " is not found");
        }
        return dishRepository.findIngredientsByDishIdWithFilters(dishId, ingredientName, ingredientPriceAround);
    }
    
    public void updateDishIngredients(Integer dishId, List<Integer> ingredientIds) throws SQLException {
        Dish dish = dishRepository.findById(dishId);
        if (dish == null) {
            throw new DishNotFoundException("Dish.id=" + dishId + " is not found");
        }
        dishRepository.updateDishIngredients(dishId, ingredientIds);
    }
}