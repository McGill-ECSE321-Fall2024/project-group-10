package com.mcgill.ecse321.GameShop.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse321.GameShop.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>,JpaRepository<Order, Integer>  {
   public Order findByTrackingNumber(String trackingNumber);
}