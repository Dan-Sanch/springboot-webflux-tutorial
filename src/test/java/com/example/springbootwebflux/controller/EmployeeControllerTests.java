package com.example.springbootwebflux.controller;

import com.example.springbootwebflux.dto.EmployeeDto;
import com.example.springbootwebflux.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@ExtendWith(SpringExtension.class)
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("Save Employee test")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        // given
        EmployeeDto employeeDto = new EmployeeDto(null, "Daniel", "Sanchez", "daniel@domain.com");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

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
        String employeeId = "DanId";
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "Daniel", "Sanchez", "daniel@domain.com");

        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

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
                .jsonPath("$.id").isEqualTo(employeeDto.getId())
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
        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(Flux.just(employeeDto, employeeDto2));

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
        String employeeId = "DanID";
        EmployeeDto updatedEmployeeDto =
                new EmployeeDto(employeeId, "Dan2", "San2", "dan2@domain.com");

        BDDMockito.given(employeeService.updateEmployee(
                ArgumentMatchers.any(EmployeeDto.class),
                ArgumentMatchers.any(String.class)
        )).willReturn(Mono.just(updatedEmployeeDto));

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

    @Test
    @DisplayName("Delete Employee test")
    public void givenEmployeeId_whenDeleteEmployee_thenStatusIsNoContent() {
        // given
        BDDMockito.given(employeeService.deleteEmployee(ArgumentMatchers.any(String.class)))
                .willReturn(Mono.empty());
        String employeeId = "DanID";

        // when
        WebTestClient.ResponseSpec response =
                webTestClient.delete()
                        .uri("/api/employees/{id}", employeeId)
                        .exchange();

        // then
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println)
                .isEmpty()
        ;
    }
}
