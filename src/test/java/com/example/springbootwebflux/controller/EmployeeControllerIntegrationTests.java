package com.example.springbootwebflux.controller;

import com.example.springbootwebflux.dto.EmployeeDto;
import com.example.springbootwebflux.repository.EmployeeRepository;
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
    private EmployeeRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
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
}
