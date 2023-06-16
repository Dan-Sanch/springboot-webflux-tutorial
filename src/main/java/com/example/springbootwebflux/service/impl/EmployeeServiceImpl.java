package com.example.springbootwebflux.service.impl;

import com.example.springbootwebflux.dto.EmployeeDto;
import com.example.springbootwebflux.entity.Employee;
import com.example.springbootwebflux.mapper.EmployeeMapper;
import com.example.springbootwebflux.repository.EmployeeRepository;
import com.example.springbootwebflux.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        Mono<EmployeeDto> savedEmployeeDto = savedEmployee.map(EmployeeMapper::mapToEmployeeDto);

        return savedEmployeeDto;
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String id) {
        Mono<Employee> savedEmployee = employeeRepository.findById(id);
        return savedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }
}
