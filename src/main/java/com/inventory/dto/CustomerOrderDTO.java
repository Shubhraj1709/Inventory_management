package com.inventory.dto;


import lombok.*;
import com.inventory.enums.OrderStatus;
import com.inventory.dto.CustomerOrderItemDTO;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDTO {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;
    private OrderStatus status;
    private List<CustomerOrderItemDTO> items;
}

