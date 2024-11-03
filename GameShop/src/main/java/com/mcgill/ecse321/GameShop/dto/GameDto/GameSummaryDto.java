package com.mcgill.ecse321.GameShop.dto.GameDto;

import com.mcgill.ecse321.GameShop.model.Game;

public class GameSummaryDto {
    
    private String title;
    private String description;
    private int price;
    private String photoUrl;

    public GameSummaryDto(Game game) {
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.price = game.getPrice();
        this.photoUrl = game.getPhotoUrl();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }


}
