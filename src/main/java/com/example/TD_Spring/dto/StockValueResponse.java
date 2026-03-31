package com.example.TD_Spring.dto;

import com.example.TD_Spring.entity.enums.Unit;

public class StockValueResponse {
    private Unit unit;
    private Double quantity;
    
    public StockValueResponse() {}
    
    public StockValueResponse(Unit unit, Double quantity) {
        this.unit = unit;
        this.quantity = quantity;
    }
    
    // Getters et Setters
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
}