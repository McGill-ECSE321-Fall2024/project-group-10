package com.mcgill.ecse321.GameShop.dto.CartDto;

public class CartSummaryDto {
    private int cartId;
    private int totalItems;
    private int totalPrice;

    public CartSummaryDto(int cartId, int totalItems, int totalPrice) {
        this.cartId = cartId;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
    }

    // Getters
    public int getCartId() {
        return cartId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
