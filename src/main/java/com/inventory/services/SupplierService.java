package com.inventory.services;

import com.inventory.dto.SupplierDTO;
import com.inventory.entities.Business;
import com.inventory.entities.Supplier;
import com.inventory.enums.NotificationType;
import com.inventory.repositories.BusinessRepository;
import com.inventory.repositories.SupplierRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final BusinessRepository businessRepository;
    private final NotificationService notificationService;

    public SupplierService(SupplierRepository supplierRepository, BusinessRepository businessRepository, NotificationService notificationService) {
        this.supplierRepository = supplierRepository;
        this.businessRepository = businessRepository;
        this.notificationService = notificationService;
    }

    public SupplierDTO addSupplier(SupplierDTO dto) {
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setBusiness(business);

        supplier = supplierRepository.save(supplier);

        notificationService.createNotification(
            "New supplier added: " + supplier.getName(),
            NotificationType.SUPPLIER_DELIVERY,
            supplier.getId()
        );

        return new SupplierDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactPerson(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                business.getId()
        );
    }

    public List<SupplierDTO> getSuppliersByBusinessId(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        return supplierRepository.findByBusiness(business).stream()
                .map(s -> new SupplierDTO(
                        s.getId(),
                        s.getName(),
                        s.getContactPerson(),
                        s.getEmail(),
                        s.getPhone(),
                        s.getAddress(),
                        s.getBusiness().getId()
                ))
                .collect(Collectors.toList());
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Optional<Supplier> existingSupplierOpt = supplierRepository.findById(id);
        if (existingSupplierOpt.isPresent()) {
            Supplier existingSupplier = existingSupplierOpt.get();

            existingSupplier.setName(updatedSupplier.getName());
            existingSupplier.setContactPerson(updatedSupplier.getContactPerson());
            existingSupplier.setEmail(updatedSupplier.getEmail());
            existingSupplier.setPhone(updatedSupplier.getPhone());
            existingSupplier.setAddress(updatedSupplier.getAddress());

            Supplier saved = supplierRepository.save(existingSupplier);

            notificationService.createNotification(
                "Supplier updated: " + existingSupplier.getName(),
                NotificationType.SUPPLIER_DELIVERY,
                existingSupplier.getId()
            );

            return saved;
        } else {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}




//package com.inventory.services;
//
//import com.inventory.dto.SupplierDTO;
//import com.inventory.entities.Business;
//import com.inventory.entities.Supplier;
//import com.inventory.enums.NotificationType;
//import com.inventory.repositories.BusinessRepository;
//import com.inventory.repositories.SupplierRepository;
//
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class SupplierService {
//    private final SupplierRepository supplierRepository;
//    private final BusinessRepository businessRepository; // ✅ Inject this
//    
//    private final NotificationService notificationService;
//
//
//    public SupplierService(SupplierRepository supplierRepository, BusinessRepository businessRepository, NotificationService notificationService) {
//        this.supplierRepository = supplierRepository;
//        this.businessRepository = businessRepository;
//        this.notificationService = notificationService;
//
//    }
//
//    public SupplierDTO addSupplier(SupplierDTO dto) {
//        Business business = businessRepository.findById(dto.getBusinessId())
//                .orElseThrow(() -> new RuntimeException("Business not found"));
//
//        Supplier supplier = new Supplier();
//        supplier.setName(dto.getName());
//        supplier.setContactPerson(dto.getContactPerson());
//        supplier.setEmail(dto.getEmail());
//        supplier.setPhone(dto.getPhone());
//        supplier.setAddress(dto.getAddress());
//        supplier.setBusiness(business); // ✅ Set business
//
//        supplier = supplierRepository.save(supplier);
//        
//        notificationService.createNotification(
//        	    "New supplier added: " + supplier.getName(),
//        	    NotificationType.SUPPLIER_DELIVERY,
//        	    supplier.getId()
//        	);
//
//
//        return new SupplierDTO(
//                supplier.getId(),
//                supplier.getName(),
//                supplier.getContactPerson(),
//                supplier.getEmail(),
//                supplier.getPhone(),
//                supplier.getAddress(),
//                business.getId()
//        );
//    }
//
//    public List<SupplierDTO> getSuppliersByBusinessId(Long businessId) {
//        Business business = businessRepository.findById(businessId)
//                .orElseThrow(() -> new RuntimeException("Business not found"));
//
//        return supplierRepository.findByBusiness(business).stream()
//                .map(s -> new SupplierDTO(
//                        s.getId(),
//                        s.getName(),
//                        s.getContactPerson(),
//                        s.getEmail(),
//                        s.getPhone(),
//                        s.getAddress(),
//                        s.getBusiness().getId()
//                ))
//                .collect(Collectors.toList());
//    }
//    
//    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
//        Optional<Supplier> existingSupplierOpt = supplierRepository.findById(id);
//        if (existingSupplierOpt.isPresent()) {
//            Supplier existingSupplier = existingSupplierOpt.get();
//
//            existingSupplier.setName(updatedSupplier.getName());
//            existingSupplier.setContactPerson(updatedSupplier.getContactPerson());
//            existingSupplier.setEmail(updatedSupplier.getEmail());
//            existingSupplier.setPhone(updatedSupplier.getPhone());
//            existingSupplier.setAddress(updatedSupplier.getAddress());
//
//            return supplierRepository.save(existingSupplier);
//        } else {
//            throw new RuntimeException("Supplier not found with id: " + id);
//        }
//    }
//
//    public void deleteSupplier(Long id) {
//        supplierRepository.deleteById(id);
//    }
//
//
//}
