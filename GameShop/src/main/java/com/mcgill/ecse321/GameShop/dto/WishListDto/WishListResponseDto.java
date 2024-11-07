package com.mcgill.ecse321.GameShop.dto.WishListDto;

import java.util.ArrayList;
import java.util.List;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.WishList;

public class WishListResponseDto {
   
    private int wishList_id;
    private String title;
    private String customerEmail;
    private GameListDto games;

    public WishListResponseDto(WishList wishList) {
        this.wishList_id = wishList.getWishList_id();
        this.title = wishList.getTitle();
        List<GameSummaryDto> gameSummaryList = new ArrayList<GameSummaryDto>();
        for(Game game : wishList.getGames()) {
            gameSummaryList.add(new GameSummaryDto(game));
        }
        this.games = new GameListDto(gameSummaryList);
        this.customerEmail = wishList.getCustomer().getEmail();
    }
    protected WishListResponseDto() {
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getWishList_id() {
        return wishList_id;
    }

    public String getTitle() {
        return title;
    }

    public GameListDto getGames() {
        return games;
    }

    public static WishListResponseDto create(WishList wishList) {
        return new WishListResponseDto(wishList);
    }
}