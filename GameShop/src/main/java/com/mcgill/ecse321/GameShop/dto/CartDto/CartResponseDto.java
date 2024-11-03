package com.mcgill.ecse321.GameShop.dto.CartDto;

import java.util.List;
import java.util.Map;

public class CartResponseDto {
    private int cartId;
    private List<Map<String, Object>> games;
    private int totalItems;
    private int totalPrice;

    public CartResponseDto(int cartId, List<Map<String, Object>> games, int totalItems, int totalPrice) {
        this.cartId = cartId;
        this.games = games;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
    }

    public int getCartId() {
        return cartId;
    }

    public List<Map<String, Object>> getGames() {
        return games;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
