package com.danielpm1982.spring_security_demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USER_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name="USERNAME", unique = true, nullable = false)
    private String username;
    @Column(name="PASSWORD", nullable = false)
    private String password;
    @Column(name="EMAIL", nullable = false)
    @Email
    private String email;
}
