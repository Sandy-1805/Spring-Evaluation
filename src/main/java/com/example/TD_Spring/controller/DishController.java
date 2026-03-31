package com.example.TD_Spring.controller;

import com.example.TD_Spring.entity.Dish;
import com.example.TD_Spring.entity.Ingredient;
import com.example.TD_Spring.exception.DishNotFoundException;
import com.example.TD_Spring.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    
    private final DishService dishService;
    
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllDishes() {
        try {
            List<Dish> dishes = dishService.getAllDishes();
            return ResponseEntity.ok(dishes);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }
    
    @GetMapping("/{id}/ingredients")
    public ResponseEntity<?> getFilteredIngredients(
            @PathVariable Integer id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround) {
        try {
            List<Ingredient> ingredients = dishService.getFilteredIngredients(id, ingredientName, ingredientPriceAround);
            return ResponseEntity.ok(ingredients);
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }
    
    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredients(
            @PathVariable Integer id,
            @RequestBody List<Integer> ingredientIds) {
        try {
            if (ingredientIds == null) {
                return ResponseEntity.badRequest().body("Request body with ingredient IDs is required");
            }
            dishService.updateDishIngredients(id, ingredientIds);
            return ResponseEntity.ok("Dish ingredients updated successfully");
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }
}