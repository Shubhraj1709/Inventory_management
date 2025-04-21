package com.inventory.services;

import com.inventory.entities.PurchaseOrder;
import com.inventory.entities.PurchaseTransaction;
import com.inventory.entities.Supplier;
import com.inventory.enums.PaymentMethod;
import com.inventory.repositories.PurchaseOrderRepository;
import com.inventory.repositories.PurchaseTransactionRepository;
import com.inventory.repositories.SupplierRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseTransactionService {

    private final PurchaseTransactionRepository transactionRepository;
    private final SupplierRepository supplierRepo;
    private final PurchaseOrderRepository orderRepo;


    public PurchaseTransaction createTransaction(Long supplierId, Long orderId, double amount, PaymentMethod  paymentMethod) {
        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        PurchaseOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));

        PurchaseTransaction transaction = new PurchaseTransaction();
        transaction.setSupplier(supplier);
        transaction.setPurchaseOrder(order);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setPaymentMethod(paymentMethod); // ✅ Add this line


        return transactionRepository.save(transaction);
    }

    public List<PurchaseTransaction> getTransactionsBySupplier(Long supplierId) {
        return transactionRepository.findBySupplierId(supplierId);
    }

    public List<PurchaseTransaction> filterTransactions(Long supplierId, LocalDateTime from, LocalDateTime to) {
        return transactionRepository.findBySupplierIdAndTransactionDateBetween(supplierId, from, to);
    }
}
