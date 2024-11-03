package com.mcgill.ecse321.GameShop.service;

import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.repository.OrderRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.exception.GameShopException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Order createOrder(Date orderDate, String note, int paymentCard, String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        Order order = new Order(orderDate, note, paymentCard, customer);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(String trackingNumber, Date orderDate, String note, int paymentCard) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }
        order.setOrderDate(orderDate);
        order.setNote(note);
        order.setPaymentCard(paymentCard);
        return orderRepository.save(order);
    }

    @Transactional
    public Order getOrderById(String trackingNumber) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

    @Transactional
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}