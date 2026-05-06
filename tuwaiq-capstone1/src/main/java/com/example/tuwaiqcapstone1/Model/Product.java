package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "ID must not be empty")
    private String id;
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;
    @NotNull(message = "Price must not be empty")
    @PositiveOrZero(message = "Price must be a positive number")
    private double price;
    @NotEmpty(message = "CategoryID must not be empty")
    private String categoryID;
    private double discount = 0;
    private double rating = 0;
}
