package com.mcgill.ecse321.GameShop.dto;

import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;

public class CustomerResponseDto extends AccountResponseDto{

    private Cart cart;

    protected CustomerResponseDto(){}

    public CustomerResponseDto(Customer customer){
        super(customer);
        this.cart = customer.getCart();
    }

    public Cart getCart(){
        return cart;
    }
}
