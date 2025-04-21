package com.inventory.controllers;

import com.inventory.dto.InvoiceDTO;
import com.inventory.dto.InvoiceRequest;
import com.inventory.entities.Invoice;
import com.inventory.services.EmailService;
import com.inventory.services.InvoiceService;
import com.inventory.services.PdfGeneratorService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfGeneratorService pdfGeneratorService;
    private final EmailService emailService;

    @PostMapping("/generate")
    public ResponseEntity<InvoiceDTO> generateInvoice(@RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.generateInvoice(request));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/mark-paid/{invoiceId}")
    public ResponseEntity<InvoiceDTO> markAsPaid(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceService.markAsPaid(invoiceId));
    }
    
    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long invoiceId) {
        // You can enhance service to return invoice DTO by ID
        Invoice invoice = invoiceService.getInvoiceEntity(invoiceId); // add this method
        InvoiceDTO dto = new InvoiceDTO(
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
        );
        return ResponseEntity.ok(dto);
    }
    
 // ✅ Add this API for PDF + Email
    @GetMapping("/send-invoice/{invoiceId}")
    public ResponseEntity<String> sendInvoice(@PathVariable Long invoiceId) {
        try {
            Invoice invoice = invoiceService.getInvoiceEntity(invoiceId);
            byte[] pdf = pdfGeneratorService.generateInvoicePdf(invoice);
            emailService.sendEmail(
                "test@fakeemail.com", // Replace with customer email in real use
                "Your Invoice " + invoice.getInvoiceNumber(),
                "Please find attached your invoice.",
                pdf
            );
            return ResponseEntity.ok("Invoice sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send invoice: " + e.getMessage());
        }
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS_OWNER')")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> allInvoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(allInvoices);
    }




}
