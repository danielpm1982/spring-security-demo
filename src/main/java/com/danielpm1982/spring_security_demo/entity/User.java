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

/*
Three sample User instances have been added to the DB, and persisted at the auth_db file at the source path folder.
As all User passwords are encrypted at the Controller layer itself, it's impossible to know the passwords for any User
saved at the DB... they'll all be encrypted... and can't be decrypted. So, for the sample users here, in case you
wanna test their authentication, their original plain-text passwords (before encryption) are:
[username]+"abcde"
for user1: user1abcde
for user2: user2abcde
for user3: user3abcde
This, if you use the spring.jpa.hibernate.ddl-auto=validate line at the application.properties. If you use the "create"
option, this sample DB will be deleted and created a new empty DB, and then it's up to you which passwords are going to
be set to each user. Anyway, they'll be irreversibly encrypted... if you forget the original raw ones, they can't be
recovered ! You'd have to reset it to a new one.
More about BCrypt here: https://docs.spring.io/spring-security/reference/features/integrations/cryptography.html .
*/
