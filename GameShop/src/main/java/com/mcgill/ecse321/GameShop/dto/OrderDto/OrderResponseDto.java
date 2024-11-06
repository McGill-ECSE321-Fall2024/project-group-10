package com.mcgill.ecse321.GameShop.dto.OrderDto;

import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameResponseDto;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseDto {
    private String trackingNumber;
    private Date orderDate;
    private String note;
    private String customerEmail;
    private List<SpecificGameResponseDto> specificGames;

    public OrderResponseDto(Order order) {
        this.trackingNumber = order.getTrackingNumber();
        this.orderDate = order.getOrderDate();
        this.note = order.getNote();
        this.customerEmail = order.getCustomer().getEmail();

    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getNote() {
        return note;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<SpecificGameResponseDto> getSpecificGames() {
        return specificGames;
    }

    // Setters
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setSpecificGames(List<SpecificGameResponseDto> specificGames) {
        this.specificGames = specificGames;
    }
}