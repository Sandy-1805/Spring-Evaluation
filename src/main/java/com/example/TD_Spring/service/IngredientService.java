package com.example.TD_Spring.service;

import com.example.TD_Spring.entity.Ingredient;
import com.example.TD_Spring.entity.StockMovement;
import com.example.TD_Spring.entity.enums.MovementTypeEnum;
import com.example.TD_Spring.entity.enums.Unit;
import com.example.TD_Spring.exception.IngredientNotFoundException;
import com.example.TD_Spring.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;
    
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    
    public List<Ingredient> getAllIngredients() throws SQLException {
        return ingredientRepository.findAll();
    }
    
    public Ingredient getIngredientById(Integer id) throws SQLException {
        return ingredientRepository.findById(id);
    }
    
    public Double getStockValueAt(Integer ingredientId, Instant at, Unit unit) throws SQLException {
        Ingredient ingredient = getIngredientById(ingredientId);
        
        double stockQuantity = 0.0;
        for (StockMovement movement : ingredient.getStockMovements()) {
            if (movement.getCreationDatetime().isBefore(at) || movement.getCreationDatetime().equals(at)) {
                if (movement.getType() == MovementTypeEnum.IN) {
                    stockQuantity += movement.getQuantity();
                } else {
                    stockQuantity -= movement.getQuantity();
                }
            }
        }
        
        // TODO: Convertir selon l'unité demandée
        return stockQuantity;
    }
}