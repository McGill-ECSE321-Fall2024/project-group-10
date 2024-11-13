package com.mcgill.ecse321.GameShop.dto.CartDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CartRequestDto {
    @NotNull(message = "Game ID is required.")
    @Min(value = 1, message = "Game ID must be positive.")
    private Integer gameId;

    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;
    private Integer platform_id;

    public CartRequestDto() {
    }

    public CartRequestDto(Integer gameId, Integer quantity, Integer platform_id) {
        this.gameId = gameId;
        this.quantity = quantity;
        this.platform_id = platform_id;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPlatformId() {
        return this.platform_id;
    }

    public void setPlatformId(Integer platform_id) {
        this.platform_id = platform_id;
    }
}