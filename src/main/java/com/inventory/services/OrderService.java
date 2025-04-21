package com.inventory.services;

import com.inventory.dto.OrderDTO;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Order;
import com.inventory.repositories.OrderRepository;
import com.inventory.repositories.BusinessOwnerRepository; // ✅ Add this import


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BusinessOwnerRepository businessOwnerRepository; // ✅ Declare repository


    public OrderService(OrderRepository orderRepository, BusinessOwnerRepository businessOwnerRepository) { // ✅ Inject repository
        this.orderRepository = orderRepository;
        this.businessOwnerRepository = businessOwnerRepository;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
    	BusinessOwner businessOwner = businessOwnerRepository.findById(orderDTO.getBusinessOwnerId())
    	        .orElseThrow(() -> new RuntimeException("Business Owner not found"));

    	Order order = new Order(orderDTO.getOrderStatus(), businessOwner);
        orderRepository.save(order);
        return new OrderDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderDTO::new).collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderDTO::new).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(orderDTO.getOrderStatus());
        orderRepository.save(order);
        return new OrderDTO(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
