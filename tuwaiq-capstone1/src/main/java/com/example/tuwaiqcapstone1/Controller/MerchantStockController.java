package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.Api.ApiResponse;
import com.example.tuwaiqcapstone1.Model.MerchantStock;
import com.example.tuwaiqcapstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/merchantStock")
@AllArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchant() {
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@Valid @RequestBody MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = merchantStockService.addMerchantStocks(merchantStock);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Merchant stock added successfully"));
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Merchant stock ID already exists"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        if (result == 3) return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add merchant stock"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatedMerchant(@PathVariable String id, @Valid @RequestBody MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (merchantStockService.updateMerchantStocks(id, merchantStock)) {
            merchantStockService.updateMerchantStocks(id, merchantStock);
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStock updated sucessfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String id) {

        if (merchantStockService.deleteMerchantStocks(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStock deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("MerchantStock not found"));
    }

    @PutMapping("/increaseStock/{productID}/{merchantID}/{stocks}")
    public ResponseEntity<?> increaseStock(@PathVariable String productID, @PathVariable String merchantID, @PathVariable int stocks) {

        int result = merchantStockService.increaseStock(productID, merchantID, stocks);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Stock increased successfully"));
        if (result == 1) return ResponseEntity.status(404).body(new ApiResponse("No stock entry found for this product and merchant"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Stocks amount must be positive"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not increase stock"));
    }

    @PostMapping("/buy/{userID}/{productID}/{merchantID}")
    public ResponseEntity<?> buyProduct(@PathVariable String userID, @PathVariable String productID, @PathVariable String merchantID) {

        String result = merchantStockService.buyProduct(userID, productID, merchantID);

        if (result.equals("Purchase successful")) {
            return ResponseEntity.status(200).body(new ApiResponse(result));
        }
        return ResponseEntity.status(400).body(new ApiResponse(result));
    }



    // Endpoint 9 - get total stock for a merchant
    @GetMapping("/totalStock/{merchantID}")
    public ResponseEntity<?> getMerchantTotalStock(@PathVariable String merchantID) {
        int total = merchantStockService.getMerchantTotalStock(merchantID);
        if (total == -1) {
            return ResponseEntity.status(404).body(new ApiResponse("No stock entries found for this merchant"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Total stock for merchant: " + total));
    }
}