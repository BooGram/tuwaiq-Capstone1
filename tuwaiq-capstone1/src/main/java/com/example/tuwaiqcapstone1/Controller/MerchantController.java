package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.Api.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Merchant;
import com.example.tuwaiqcapstone1.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/merchant")
@AllArgsConstructor
public class MerchantController {

        private final MerchantService merchantService;

        @GetMapping("/get")
        public ResponseEntity<?> getMerchant(){
            return ResponseEntity.status(200).body(merchantService.getMerchants());
        }
    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = merchantService.addMerchants(merchant);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully"));
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Merchant ID already exists"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add merchant"));
    }


        @PutMapping("/update/{id}")
        public ResponseEntity<?> updatedMerchant(@PathVariable String id, @Valid @RequestBody Merchant merchant, Errors errors) {
            if (errors.hasErrors()) {
                return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            }
            if (merchantService.updateMerchants(id, merchant)) {
                merchantService.updateMerchants(id, merchant);
                return ResponseEntity.status(200).body(new ApiResponse("Merchant updated sucessfully"));
            }
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        }

        @DeleteMapping("delete/{id}")
        public ResponseEntity<?> deleteMerchant(@PathVariable String id) {

            if (merchantService.deleteMerchants(id)) {
                return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));
            }
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        }

    }
