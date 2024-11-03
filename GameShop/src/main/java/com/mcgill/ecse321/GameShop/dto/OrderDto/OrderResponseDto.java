package com.mcgill.ecse321.GameShop.dto.OrderDto;

import java.util.Date;

public class OrderResponseDto {
    private String trackingNumber;
    private Date orderDate;
    private String note;
    private int paymentCard;
    private String customerEmail;

    public OrderResponseDto(String trackingNumber, Date orderDate, String note, int paymentCard, String customerEmail) {
        this.trackingNumber = trackingNumber;
        this.orderDate = orderDate;
        this.note = note;
        this.paymentCard = paymentCard;
        this.customerEmail = customerEmail;
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
}
