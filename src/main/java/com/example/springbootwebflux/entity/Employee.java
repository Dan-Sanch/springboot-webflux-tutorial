package com.example.springbootwebflux.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection="employees")
public class Employee {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
