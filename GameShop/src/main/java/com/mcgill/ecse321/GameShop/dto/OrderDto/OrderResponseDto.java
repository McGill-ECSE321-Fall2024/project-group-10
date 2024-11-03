package com.mcgill.ecse321.GameShop.dto.OrderDto;

import com.mcgill.ecse321.GameShop.model.Order;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseDto {
    private String trackingNumber;
    private Date orderDate;
    private String note;
    private int paymentCard;
    private String customerEmail;
    // private List<SpecificGameDto> specificGames;

    // Constructor that accepts an Order entity and maps SpecificGame list
    public OrderResponseDto(Order order) {
        this.trackingNumber = order.getTrackingNumber();
        this.orderDate = order.getOrderDate();
        this.note = order.getNote();
        this.customerEmail = order.getCustomer().getEmail();
        /*
         * this.specificGames = order.getSpecificGames().stream()
         * .map(SpecificGameDto::new)
         * .collect(Collectors.toList());
         */
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

    public int getPaymentCard() {
        return paymentCard;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

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
    /*
     * public List<SpecificGameDto> getSpecificGames() {
     * return specificGames;
     * }
     */
}
