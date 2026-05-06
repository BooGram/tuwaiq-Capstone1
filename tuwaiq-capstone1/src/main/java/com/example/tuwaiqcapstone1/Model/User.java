package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "ID must not be empty")
    private String id;

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 3, message = "Username has to be at least 3 length long")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$", message = "Password must be at least 6 characters and contain both letters and digits")
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid format")
    private String email;

    @NotEmpty(message = "Role must not be empty")
    @Pattern(regexp = "Admin|Customer", message = "Role must be either 'Admin' or 'Customer'")
    private String role;

    @NotNull(message = "Balance must not be empty")
    @PositiveOrZero(message = "Balance must not be negative")
    private double balance;

    private ArrayList<String> purchasedProductIDs = new ArrayList<>();
}