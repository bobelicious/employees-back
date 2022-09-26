package com.augusto.employees.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String cpfOrEmail;
    private String password;
}
