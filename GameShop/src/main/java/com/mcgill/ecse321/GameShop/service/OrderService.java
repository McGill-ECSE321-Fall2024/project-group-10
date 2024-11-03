package com.mcgill.ecse321.GameShop.service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.repository.OrderRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.*;

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
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found with the specified email.");
        }

        Order order = new Order(orderDate, note, paymentCard, customer);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(String trackingNumber, Date orderDate, String note, int paymentCard) {
        Order order = getOrderById(trackingNumber);

        if (orderDate != null) {
            order.setOrderDate(orderDate);
        }
        if (note != null) {
            order.setNote(note);
        }
        order.setPaymentCard(paymentCard);

        return orderRepository.save(order);
    }

    @Transactional
    public Order getOrderById(String trackingNumber) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber);
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    "Order with specified tracking number does not exist.");
        }
        return order;
    }

    @Transactional
    public Iterable<Order> getOrders() {
        return orderRepository.findAll();
    }
}
