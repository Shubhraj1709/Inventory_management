package com.inventory.services;

import com.inventory.dto.InvoiceDTO;
import com.inventory.dto.InvoiceItemDTO;
import com.inventory.dto.InvoiceRequest;
import com.inventory.entities.Invoice;
import com.inventory.entities.Order;
import com.inventory.enums.PaymentStatus;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.InvoiceRepository;
import com.inventory.repositories.OrderRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, OrderRepository orderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
    }

    public InvoiceDTO generateInvoice(Long orderId, double taxRate, double discount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        double taxAmount = order.getTotalAmount() * (taxRate / 100);
        double discountAmount = order.getTotalAmount() * (discount / 100);
        double finalAmount = order.getTotalAmount() + taxAmount - discountAmount;

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setTotalAmount(order.getTotalAmount());
        invoice.setTaxAmount(taxAmount);
        invoice.setDiscountAmount(discountAmount);
        invoice.setFinalAmount(finalAmount);
        invoice.setPaymentStatus(PaymentStatus.UNPAID);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setOrder(order);
        invoice.setBusinessId(order.getBusinessOwner().getId());


        Invoice savedInvoice = invoiceRepository.save(invoice);
        return new InvoiceDTO(
        		null,
                savedInvoice.getInvoiceNumber(),
                savedInvoice.getTotalAmount(),
                savedInvoice.getTaxAmount(),
                savedInvoice.getDiscountAmount(),
                savedInvoice.getFinalAmount(),
                savedInvoice.getPaymentStatus(),
                orderId,
                null,
                null
        );
    }

    public InvoiceDTO generateInvoice(InvoiceRequest request) {
        return generateInvoice(request.getOrderId(), request.getTaxRate(), request.getDiscount());
    }

    public InvoiceDTO markAsPaid(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoiceRepository.save(invoice);

        return new InvoiceDTO(
        		invoice.getId(), 
                invoice.getInvoiceNumber(),
                invoice.getTotalAmount(),
                invoice.getTaxAmount(),
                invoice.getDiscountAmount(),
                invoice.getFinalAmount(),
                invoice.getPaymentStatus(),
                invoice.getOrder().getId(),
                null,
                null
        );
    }
    
    public void processUnpaidInvoices() {
        List<Invoice> unpaidInvoices = invoiceRepository.findAllUnpaidInvoices();
        // For now, let's just log or print them
        unpaidInvoices.forEach(invoice -> {
            System.out.println("Unpaid Invoice ID: " + invoice.getId());
            // Or perform auto-reminders, flag them, etc.
        });
    }
    
    public Invoice getInvoiceEntity(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }
    
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();

        return invoices.stream().map(invoice -> new InvoiceDTO(
        		null,
                invoice.getInvoiceNumber(),
                invoice.getTotalAmount(),
                invoice.getTaxAmount(),
                invoice.getDiscountAmount(),
                invoice.getFinalAmount(),
                invoice.getPaymentStatus(),
                invoice.getOrder().getId(),
                null,
                null
        )).collect(Collectors.toList());
    }


}
