package com.example.tuwaiqcapstone1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "id must not be empty")
    private String id;
    @NotEmpty(message = "productID must not be empty")
    private String productID;
    @NotEmpty(message = "merchantID must not be empty")
    private String merchantID;
    @Min(value = 10, message = "Stock must be at least 10")
    private int stock;
}
