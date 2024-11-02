package com.mcgill.ecse321.GameShop.dto;

import com.mcgill.ecse321.GameShop.model.Employee;

public class StaffResponseDto extends AccountResponseDto{
    
    public StaffResponseDto(Employee employee){
        super(employee);
    }
}
