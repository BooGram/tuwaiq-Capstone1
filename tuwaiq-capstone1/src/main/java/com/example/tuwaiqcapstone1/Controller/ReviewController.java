package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.Api.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Review;
import com.example.tuwaiqcapstone1.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/get")
    public ResponseEntity<?> getReviews() {
        return ResponseEntity.status(200).body(reviewService.getReviews());
    }

    // 5 - User Rating/Review
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@Valid @RequestBody Review review, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = reviewService.addReview(review);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Review ID already exists"));
        if (result == 2) return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        if (result == 3) return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add review"));
    }

    @GetMapping("/product/{productID}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable String productID) {
        var result = reviewService.getReviewsByProduct(productID);
        if (result == null) return ResponseEntity.status(404).body(new ApiResponse("No reviews found for this product"));
        return ResponseEntity.status(200).body(result);
    }
}
