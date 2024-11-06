package com.mcgill.ecse321.GameShop.controller;

import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderRequestDto;
import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderResponseDto;
import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderListDto;
import com.mcgill.ecse321.GameShop.dto.OrderDto.OrderSummaryDto;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// get all orders/specific games, get gamebyid, add game to order,  
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** Create a new order */
    @PostMapping("/orders")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto request) {
        Order order = orderService.createOrder(
                request.getOrderDate(),
                request.getNote(),
                request.getPaymentCard(),
                request.getCustomerEmail());
        return new OrderResponseDto(order);
    }

    @GetMapping("/orders/{trackingNumber}")
    public OrderResponseDto getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        Order order = orderService.getOrderById(trackingNumber);
        return new OrderResponseDto(order);
    }

    @GetMapping("/orders")
    public OrderListDto getAllOrders() {
        List<OrderSummaryDto> dtos = new ArrayList<>();
        for (Order order : orderService.getAllOrders()) {
            dtos.add(new OrderSummaryDto(order));
        }
        return new OrderListDto(dtos);
    }

    @PutMapping("/orders/{trackingNumber}")
    public OrderResponseDto updateOrder(
            @PathVariable String trackingNumber,
            @RequestBody OrderRequestDto request) {
        Order order = orderService.updateOrder(
                trackingNumber,
                request.getOrderDate(),
                request.getNote(),
                request.getPaymentCard());
        return new OrderResponseDto(order);
    }
}