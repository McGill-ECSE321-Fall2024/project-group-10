package com.mcgill.ecse321.GameShop.dto.GameDto;

import com.mcgill.ecse321.GameShop.model.Game.GameStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GameRequestDto {
    
    @NotBlank(message = "Game title cannot be empty")
    private String aTitle;

    @NotBlank(message = "Game description cannot be empty")
    private String aDescription;

    @Positive(message = "Game price cannot be negative")
    private int aPrice;

    @NotNull(message = "Game status cannot be null")
    private GameStatus aGameStatus;

    @Positive(message = "Game stock quantity cannot be negative")
    private int aStockQuantity;

    @NotBlank(message = "Game photo URL cannot be empty")
    private String aPhotoUr;

    public GameRequestDto(String aTitle, String aDescription, int aPrice, GameStatus aGameStatus, int aStockQuantity, String aPhotoUr) {
        this.aTitle = aTitle;
        this.aDescription = aDescription;
        this.aPrice = aPrice;
        this.aGameStatus = aGameStatus;
        this.aStockQuantity = aStockQuantity;
        this.aPhotoUr = aPhotoUr;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaDescription(String aDescription) {
        this.aDescription = aDescription;
    }

    public int getaPrice() {
        return aPrice;
    }  

    public void setaPrice(int aPrice) {
        this.aPrice = aPrice;
    }

    public GameStatus getaGameStatus() {
        return aGameStatus;
    }

    public void setaGameStatus(GameStatus aGameStatus) {
        this.aGameStatus = aGameStatus;
    }

    public int getaStockQuantity() {
        return aStockQuantity;
    }  

    public void setaStockQuantity(int aStockQuantity) {
        this.aStockQuantity = aStockQuantity;
    }

    public String getaPhotoUr() {
        return aPhotoUr;
    }

    public void setaPhotoUr(String aPhotoUr) {
        this.aPhotoUr = aPhotoUr;
    }
}
