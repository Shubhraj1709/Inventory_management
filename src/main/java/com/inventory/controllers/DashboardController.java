package com.inventory.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.repositories.EmployeeRepository;
import com.inventory.repositories.InvoiceRepository;
import com.inventory.repositories.OrderRepository;
import com.inventory.repositories.ProductRepository;
import com.inventory.repositories.SupplierRepository;

//DashboardController.java
@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

 @Autowired private ProductRepository productRepo;
 @Autowired private OrderRepository orderRepo;
 @Autowired private EmployeeRepository employeeRepo;
 @Autowired private SupplierRepository supplierRepo;
 @Autowired private InvoiceRepository invoiceRepo;

 @GetMapping("/counts")
 public ResponseEntity<Map<String, Long>> getCounts() {
     Map<String, Long> counts = new HashMap<>();
     counts.put("products", productRepo.count());
     counts.put("orders", orderRepo.count());
     counts.put("employees", employeeRepo.count());
     counts.put("suppliers", supplierRepo.count());
     counts.put("invoices", invoiceRepo.count());
     return ResponseEntity.ok(counts);
 }
}
