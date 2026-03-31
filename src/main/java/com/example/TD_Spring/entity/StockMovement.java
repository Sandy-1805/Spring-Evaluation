package com.example.TD_Spring.entity;

import com.example.TD_Spring.entity.enums.MovementTypeEnum;
import com.example.TD_Spring.entity.enums.Unit;
import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private MovementTypeEnum type;
    private Double quantity;
    private Unit unit;
    private Instant creationDatetime;
    
    public StockMovement() {}
    
    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
    
    public MovementTypeEnum getType() { return type; }
    public void setType(MovementTypeEnum type) { this.type = type; }
    
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    
    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }
}