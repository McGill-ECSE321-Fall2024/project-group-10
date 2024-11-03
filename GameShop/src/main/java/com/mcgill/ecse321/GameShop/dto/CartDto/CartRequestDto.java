package com.mcgill.ecse321.GameShop.dto.CartDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartRequestDto {
    @NotNull(message = "Game ID is required.")
    private Integer gameId;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private int quantity;

    public CartRequestDto() {
    }

    public CartRequestDto(Integer gameId, int quantity) {
        this.gameId = gameId;
        this.quantity = quantity;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}