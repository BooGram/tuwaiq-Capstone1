package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {

    @NotEmpty(message = "Review ID must not be empty")
    private String id;

    @NotEmpty(message = "User ID must not be empty")
    private String userID;

    @NotEmpty(message = "Product ID must not be empty")
    private String productID;

    @Min(value = 1, message = "Rate must be at least 1")
    @Max(value = 5, message = "Rate must not exceed 5")
    private int rate;

    @NotEmpty(message = "Comment must not be empty")
    @Size(min = 3, message = "Comment must be at least 3 characters")
    private String comment;
}