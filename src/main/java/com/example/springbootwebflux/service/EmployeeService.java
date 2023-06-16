package com.example.springbootwebflux.service;

import com.example.springbootwebflux.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> getEmployee(String id);
    Flux<EmployeeDto> getAllEmployees();
}
