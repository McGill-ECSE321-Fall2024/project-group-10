package com.mcgill.ecse321.GameShop.dto.OrderDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class OrderRequestDto {
    @NotNull(message = "Order date cannot be null")
    @PastOrPresent(message = "Order date must be in the past or present")
    private Date orderDate;

    @NotEmpty(message = "Note cannot be empty")
    @Size(max = 255, message = "Note cannot be longer than 255 characters")
    private String note;

    @NotNull(message = "Payment card cannot be null")
    private int paymentCard;

    @NotEmpty(message = "Customer email cannot be empty")
    @Email(message = "Customer email should be valid")
    private String customerEmail;

    @NotEmpty(message = "Specific game IDs cannot be empty")
    private List<Integer> specificGameIds;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getNote() {
        return note;
    }
    protected OrderRequestDto() {
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(int paymentCard) {
        this.paymentCard = paymentCard;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<Integer> getSpecificGameIds() {
        return specificGameIds;
    }

    public void setSpecificGameIds(List<Integer> specificGameIds) {
        this.specificGameIds = specificGameIds;
    }
}