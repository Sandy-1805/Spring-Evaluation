package com.example.TD_Spring.controller;

import com.example.TD_Spring.dto.StockValueResponse;
import com.example.TD_Spring.entity.Ingredient;
import com.example.TD_Spring.entity.enums.Unit;
import com.example.TD_Spring.exception.IngredientNotFoundException;
import com.example.TD_Spring.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    
    private final IngredientService ingredientService;
    
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }
    
    // a) GET /ingredients
    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }
    
    // b) GET /ingredients/{id}
    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable Integer id) {
        return ingredientService.getIngredientById(id);
    }
    
    // c) GET /ingredients/{id}/stock?at={temporal}&unit={unit}
    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStockValue(
            @PathVariable Integer id,
            @RequestParam Instant at,
            @RequestParam Unit unit) {
        
        // Vérifier les paramètres obligatoires
        if (at == null || unit == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Either mandatory query parameter 'at' or 'unit' is not provided.");
        }
        
        Double stockQuantity = ingredientService.getStockValueAt(id, at, unit);
        StockValueResponse response = new StockValueResponse(unit, stockQuantity);
        
        return ResponseEntity.ok(response);
    }
    
    // Gestionnaire d'exception pour IngredientNotFoundException
    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<String> handleIngredientNotFound(IngredientNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}