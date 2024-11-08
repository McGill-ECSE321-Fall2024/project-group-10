package com.mcgill.ecse321.GameShop.controller;

import com.mcgill.ecse321.GameShop.dto.OrderDto.*;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameResponseDto;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto request) {
        Order order = orderService.createOrder(
                request.getOrderDate(),
                request.getNote(),
                request.getPaymentCard(),
                request.getCustomerEmail());

        List<SpecificGameResponseDto> specificGames = orderService.getSpecificGamesByOrder(order.getTrackingNumber())
                .stream()
                .map(SpecificGameResponseDto::new)
                .collect(Collectors.toList());

        return OrderResponseDto.create(order, specificGames);
    }

    @GetMapping("/orders/{trackingNumber}")
    public OrderResponseDto getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        Order order = orderService.getOrderById(trackingNumber);
        List<SpecificGameResponseDto> specificGames = orderService.getSpecificGamesByOrder(trackingNumber).stream()
                .map(SpecificGameResponseDto::new)
                .collect(Collectors.toList());
        return OrderResponseDto.create(order, specificGames);
    }

    @GetMapping("/orders")
    public OrderListDto getAllOrders() {
        List<OrderSummaryDto> dtos = orderService.getAllOrders().stream()
                .map(OrderSummaryDto::new)
                .collect(Collectors.toList());
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

        List<SpecificGameResponseDto> specificGames = orderService.getSpecificGamesByOrder(order.getTrackingNumber())
                .stream()
                .map(SpecificGameResponseDto::new)
                .collect(Collectors.toList());

        return OrderResponseDto.create(order, specificGames);
    }

    @PostMapping("/orders/{trackingNumber}/games")
    public void addGameToOrder(
            @PathVariable String trackingNumber,
            @RequestBody OrderAddGameRequestDto request) {
        orderService.addGameToOrder(trackingNumber, request.getGameId(), request.getQuantity());
    }

    @GetMapping("/orders/{trackingNumber}/specific-games")
    public List<SpecificGameResponseDto> getSpecificGamesByOrder(@PathVariable String trackingNumber) {
        List<SpecificGame> specificGames = orderService.getSpecificGamesByOrder(trackingNumber);
        return specificGames.stream()
                .map(SpecificGameResponseDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/orders/{trackingNumber}/return/{specificGameId}")
    public void returnGame(
            @PathVariable String trackingNumber,
            @PathVariable int specificGameId) {
        orderService.returnGame(trackingNumber, specificGameId);
    }
}