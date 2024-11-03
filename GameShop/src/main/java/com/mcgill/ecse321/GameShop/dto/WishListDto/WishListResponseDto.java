package com.mcgill.ecse321.GameShop.dto.WishListDto;

import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.WishList;

public class WishListResponseDto {
   
    private int wishList_id;
    private String title;
    private String customerEmail;

    public WishListResponseDto(WishList wishList) {
        this.wishList_id = wishList.getWishList_id();
        this.title = wishList.getTitle();
        this.customerEmail = wishList.getCustomer().getAddress();
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
}