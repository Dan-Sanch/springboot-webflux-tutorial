package com.example.springbootwebflux.controller;

import com.example.springbootwebflux.dto.EmployeeDto;
import com.example.springbootwebflux.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto){
        return employeeService.saveEmployee(employeeDto);
    }

    @GetMapping("/{id}")
    public Mono<EmployeeDto> getEmployeeById(@PathVariable String id){
        return employeeService.getEmployee(id);
    }

    @GetMapping
    public Flux<EmployeeDto> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PutMapping("/{id}")
    public Mono<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto,
                                            @PathVariable("id") String employeeId)
    {
        return employeeService.updateEmployee(employeeDto, employeeId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable("id") String employeeId)
    {
        return employeeService.deleteEmployee(employeeId);
    }
}
