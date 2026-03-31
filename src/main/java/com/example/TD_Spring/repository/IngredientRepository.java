package com.example.TD_Spring.repository;

import com.example.TD_Spring.entity.Ingredient;
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
public class IngredientRepository {
    
    private final Connection connection;
    
    public IngredientRepository(Connection connection) {
        this.connection = connection;
    }
    
    // GET all ingredients
    public List<Ingredient> findAll() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category FROM ingredient";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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
        return ingredients;
    }
    
    // GET ingredient by id
    public Ingredient findById(Integer id) throws SQLException {
        String sql = "SELECT id, name, price, category FROM ingredient WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setPrice(rs.getDouble("price"));
                    String categoryStr = rs.getString("category");
                    if (categoryStr != null) {
                        ingredient.setCategory(CategoryEnum.valueOf(categoryStr));
                    }
                    return ingredient;
                }
                return null;
            }
        }
    }
    
    // GET stock value at specific date
    public Double getStockValueAt(Integer ingredientId, String at) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(CASE 
                WHEN type = 'IN' THEN quantity 
                WHEN type = 'OUT' THEN -quantity 
                ELSE 0 END), 0) as stock
            FROM stock_movement
            WHERE id_ingredient = ? AND creation_datetime <= ?::timestamp
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            ps.setString(2, at);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("stock");
                }
                return 0.0;
            }
        }
    }
}