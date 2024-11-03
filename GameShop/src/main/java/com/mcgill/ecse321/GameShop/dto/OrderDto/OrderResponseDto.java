package com.mcgill.ecse321.GameShop.dto.OrderDto;

import com.mcgill.ecse321.GameShop.model.Order;

import java.util.Date;

public class OrderResponseDto {
    private String trackingNumber;
    private Date orderDate;
    private String note;
    private String customerEmail;

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
}
