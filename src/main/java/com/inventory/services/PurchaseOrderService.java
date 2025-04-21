package com.inventory.services;

import com.inventory.entities.PurchaseOrder;
import com.inventory.entities.Supplier;
import com.inventory.enums.OrderStatus;
import com.inventory.repositories.PurchaseOrderRepository;
import com.inventory.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository orderRepo;
    private final SupplierRepository supplierRepo;

    public PurchaseOrder createOrder(Long supplierId, double totalAmount) {
        Supplier supplier = supplierRepo.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

        PurchaseOrder order = new PurchaseOrder();
        order.setSupplier(supplier);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepo.save(order);
    }

    public List<PurchaseOrder> getOrdersBySupplier(Long supplierId) {
        return orderRepo.findBySupplierId(supplierId);
    }

    public PurchaseOrder getOrderById(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public PurchaseOrder updateOrderStatus(Long orderId, OrderStatus newStatus) {
        PurchaseOrder order = getOrderById(orderId);
        order.setStatus(newStatus);
        return orderRepo.save(order);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepo.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        orderRepo.deleteById(orderId);
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepo.findAll();
    }
}
