package com.inventory.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class EmployeeUpdateRequest {
    private List<String> permissions;
}
