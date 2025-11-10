package com.danielpm1982.spring_security_demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOInput {
    private String username;
    private String password;
    private String email;
}
