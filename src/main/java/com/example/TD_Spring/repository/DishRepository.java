package com.example.TD_Spring.repository;

import com.example.TD_Spring.entity.Dish;
import com.example.TD_Spring.entity.Ingredient;
import com.example.TD_Spring.entity.enums.DishTypeEnum;
import com.example.TD_Spring.entity.enums.CategoryEnum;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {
    
    private final Connection connection;
    
    public DishRepository(Connection connection) {
        this.connection = connection;
    }
    
    // GET all dishes
    public List<Dish> findAll() throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT id, name, dish_type, selling_price FROM dish";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                String dishTypeStr = rs.getString("dish_type");
                if (dishTypeStr != null) {
                    dish.setDishType(DishTypeEnum.valueOf(dishTypeStr));
                }
                dish.setSellingPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);
                dishes.add(dish);
            }
        }
        return dishes;
    }
    
    // GET dish by id
    public Dish findById(Integer id) throws SQLException {
        String sql = "SELECT id, name, dish_type, selling_price FROM dish WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    String dishTypeStr = rs.getString("dish_type");
                    if (dishTypeStr != null) {
                        dish.setDishType(DishTypeEnum.valueOf(dishTypeStr));
                    }
                    dish.setSellingPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);
                    return dish;
                }
                return null;
            }
        }
    }
    
    // GET ingredients by dish id with filters
    public List<Ingredient> findIngredientsByDishIdWithFilters(Integer dishId, String ingredientName, Double ingredientPriceAround) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT i.id, i.name, i.price, i.category
            FROM ingredient i
            JOIN dish_ingredient di ON di.id_ingredient = i.id
            WHERE di.id_dish = ?
        """);
        
        List<Object> params = new ArrayList<>();
        params.add(dishId);
        
        if (ingredientName != null && !ingredientName.isEmpty()) {
            sql.append(" AND i.name ILIKE ?");
            params.add("%" + ingredientName + "%");
        }
        
        if (ingredientPriceAround != null) {
            sql.append(" AND i.price BETWEEN ? AND ?");
            params.add(ingredientPriceAround - 50);
            params.add(ingredientPriceAround + 50);
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setPrice(rs.getDouble("price"));
                    String categoryStr = rs.getString("category");
                    if (categoryStr != null) {
                        ingredient.setCategory(CategoryEnum.valueOf(categoryStr));
                    }
                    ingredients.add(ingredient);
                }
            }
        }
        return ingredients;
    }
    
    // UPDATE dish ingredients (detach all + attach new)
    public void updateDishIngredients(Integer dishId, List<Integer> ingredientIds) throws SQLException {
        // Détacher tous les ingrédients
        String deleteSql = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        }
        
        // Attacher les nouveaux ingrédients
        String insertSql = "INSERT INTO dish_ingredient (id_ingredient, id_dish) VALUES (?, ?)";
        for (Integer ingredientId : ingredientIds) {
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, ingredientId);
                ps.setInt(2, dishId);
                ps.executeUpdate();
            }
        }
    }
}