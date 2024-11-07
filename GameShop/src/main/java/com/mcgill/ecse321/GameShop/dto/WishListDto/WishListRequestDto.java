package com.mcgill.ecse321.GameShop.dto.WishListDto;


import jakarta.validation.constraints.NotBlank;

public class WishListRequestDto {
    @NotBlank(message ="WishList name cannot be empty")
    private String wishListName;
    @NotBlank(message = "Email cannot be empty")
    private String customerEmail;

    protected WishListRequestDto() {
    }
    public WishListRequestDto(String wishListName, String customerEmail) {
        this.wishListName = wishListName;
        this.customerEmail = customerEmail;
    }
    public String getWishListName() {
        return wishListName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
    //public Iterable<Gto> 
}
