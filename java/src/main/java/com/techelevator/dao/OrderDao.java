package com.techelevator.dao;

import com.techelevator.model.Order;
import com.techelevator.model.OrderDto;

import java.util.List;

public interface OrderDao {

    Order getOrderById(int orderId);

    List<Order> getOrdersByCookoutId(int cookoutId);

    Order createOrder(OrderDto orderDto);

    int finishOrder(int orderId);
}
