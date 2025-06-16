package com.inventory.services;

import com.inventory.dto.OrderDTO;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Order;
import com.inventory.enums.NotificationType;
import com.inventory.repositories.OrderRepository;
import com.inventory.repositories.BusinessOwnerRepository; // ✅ Add this import


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BusinessOwnerRepository businessOwnerRepository; // ✅ Declare repository

    private final NotificationService notificationService;


    public OrderService(OrderRepository orderRepository, BusinessOwnerRepository businessOwnerRepository , NotificationService notificationService) { // ✅ Inject repository
        this.orderRepository = orderRepository;
        this.businessOwnerRepository = businessOwnerRepository;
        this.notificationService = notificationService;
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
        
        notificationService.createNotification(
        	    "Order updated: ID #" + order.getId() + " now has status " + order.getOrderStatus(),
        	    NotificationType.ORDER_UPDATE,
        	    order.getId()
        	);

        return new OrderDTO(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
