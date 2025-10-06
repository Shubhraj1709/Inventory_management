package com.inventory.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private String planName;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}
