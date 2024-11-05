package com.mcgill.ecse321.GameShop.dto.GameDto;

import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryListDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;

public class GameResponseDto {
        
        private int aGame_id;
        private String aTitle;
        private String aDescription;
        private int aPrice;
        private GameStatus aGameStatus;
        private int aStockQuantity;
        private String aPhotoUrl;
        private CategoryListDto categories;

        protected GameResponseDto() {
        }

        public GameResponseDto(Game aGame) {
            this.aGame_id = aGame.getGame_id();
            this.aTitle = aGame.getTitle();
            this.aDescription = aGame.getDescription();
            this.aPrice = aGame.getPrice();
            this.aGameStatus = aGame.getGameStatus();
            this.aStockQuantity = aGame.getStockQuantity();
            this.aPhotoUrl = aGame.getPhotoUrl();
            this.categories = CategoryListDto.convertToCategoryListDto(aGame.getCategories());
        }

        // TODO check with team: Static factory method to create a GameResponseDto from a Game instance
        public static GameResponseDto create(Game aGame) {
            return new GameResponseDto(aGame);
        }

        public int getaGame_id() {
            return aGame_id;
        }

        public String getaTitle() {
            return aTitle;
        }

        public String getaDescription() {
            return aDescription;
        }

        public int getaPrice() {
            return aPrice;
        }

        public GameStatus getaGameStatus() {
            return aGameStatus;
        }

        public int getaStockQuantity() {
            return aStockQuantity;
        }

        public String getaPhotoUr() {
            return aPhotoUrl;
        }
    
}
