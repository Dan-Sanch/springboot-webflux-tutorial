package com.example.springbootwebflux.service.impl;

import com.example.springbootwebflux.dto.EmployeeDto;
import com.example.springbootwebflux.entity.Employee;
import com.example.springbootwebflux.mapper.EmployeeMapper;
import com.example.springbootwebflux.repository.EmployeeRepository;
import com.example.springbootwebflux.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> allEmployees = employeeRepository.findAll();

        return allEmployees
                .map(EmployeeMapper::mapToEmployeeDto)
                .switchIfEmpty(Flux.empty())
            ;
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId) {
        Mono<Employee> employeeMono = employeeRepository.findById(employeeId);
        // We should check that the Employee actually exists

        // NOTE: flatMap doesn't really do much, besides unpacking the Employee only within the callback Function,
        // probably making the sequence asynchronous
        // Functionality wise, we could just unpack the found Employee, modify it, and then save it, all of it
        // sequentially outside the callback function
        Mono<Employee> updatedEmployee = employeeMono.flatMap(existingEmployee -> {
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());

            return employeeRepository.save(existingEmployee);
        });

        return updatedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        return employeeRepository.deleteById(employeeId);
    }
}
