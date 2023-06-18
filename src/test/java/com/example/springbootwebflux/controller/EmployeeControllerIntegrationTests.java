package com.example.springbootwebflux.controller;

import com.example.springbootwebflux.dto.EmployeeDto;
import com.example.springbootwebflux.repository.EmployeeRepository;
import com.example.springbootwebflux.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Note: without annotations or settings telling otherwise, we'll use the same DB service defined by the app
public class EmployeeControllerIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Save Employee test")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        // given
        EmployeeDto employeeDto = new EmployeeDto(null, "Daniel", "Sanchez", "daniel@domain.com");

        // when
        WebTestClient.ResponseSpec response =
                webTestClient.post()
                        .uri("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(Mono.just(employeeDto), EmployeeDto.class)
                        .exchange();

        // then
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail())
        ;
    }

    @Test
    @DisplayName("Get Employee test")
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployee() {
        // given
        EmployeeDto employeeDto = new EmployeeDto(null, "Daniel", "Sanchez", "daniel@domain.com");
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();
        String employeeId = savedEmployee.getId();

        // when
        WebTestClient.ResponseSpec response =
                webTestClient.get()
                        .uri("/api/employees/{id}", employeeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange();

        // then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(employeeId)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail())
        ;
    }

    @Test
    @DisplayName("Get All Employees test")
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        // given
        EmployeeDto employeeDto = new EmployeeDto("DanID", "Daniel", "Sanchez", "daniel@domain.com");
        EmployeeDto employeeDto2 = new EmployeeDto("Dan2ID", "Daniel2", "Sanchez2", "daniel2@domain.com");
        employeeService.saveEmployee(employeeDto).block();
        employeeService.saveEmployee(employeeDto2).block();

        // when
        WebTestClient.ResponseSpec response =
                webTestClient.get()
                        .uri("/api/employees")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange();

        // then
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println)
                .hasSize(2)
        ;
    }

    @Test
    @DisplayName("Update Employee test")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        EmployeeDto employeeDto = new EmployeeDto(null, "Daniel", "Sanchez", "daniel@domain.com");
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        String employeeId = savedEmployee.getId();
        EmployeeDto updatedEmployeeDto =
                new EmployeeDto(employeeId, "Dan2", "San2", "dan2@domain.com");

        // when
        WebTestClient.ResponseSpec response =
                webTestClient.put()
                        .uri("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(Mono.just(updatedEmployeeDto), EmployeeDto.class)
                        .exchange();

        // then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployeeDto.getEmail())
        ;
    }
}
