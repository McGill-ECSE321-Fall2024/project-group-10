package com.mcgill.ecse321.GameShop.dto.SpecificGameDto;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.model.SpecificGame.ItemStatus;

public class SpecificGameSummaryDto {
    
    private int specificGame_id;
    private ItemStatus itemStatus;
    private GameSummaryDto game;

    public SpecificGameSummaryDto(SpecificGame specificGame) {
        this.specificGame_id = specificGame.getSpecificGame_id();
        this.itemStatus = specificGame.getItemStatus();
        GameSummaryDto gameDto = new GameSummaryDto(specificGame.getGames());
        this.game = gameDto;
    }

    public int getSpecificGame_id() {
        return specificGame_id;
    }
    protected SpecificGameSummaryDto() {
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public GameSummaryDto getGame() {
        return game;
    }

}
