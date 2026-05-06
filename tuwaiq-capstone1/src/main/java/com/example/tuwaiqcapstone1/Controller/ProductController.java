package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.Api.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Model.Review;
import com.example.tuwaiqcapstone1.Service.MerchantStockService;
import com.example.tuwaiqcapstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.status(200).body(productService.getProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = productService.addProducts(product);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Product ID already exists"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add product"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (productService.updateProducts(id, product)) {
            return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        if (productService.deleteProducts(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
    }

    // Endpoint 2 - recommendation based on user balance
    @GetMapping("/recommendation/{userID}")
    public ResponseEntity<?> recommendation(@PathVariable String userID) {
        ArrayList<Product> result = productService.recommendations(userID);
        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }
        if (result.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No products available within your balance"));
        }
        return ResponseEntity.status(200).body(result);
    }

    // Endpoint 3 - filter by category
    @GetMapping("/category/{categoryID}")
    public ResponseEntity<?> filterCategory(@PathVariable String categoryID) {
        ArrayList<Product> result = productService.filterCategory(categoryID);
        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Category not found"));
        }
        if (result.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("Category exists but has no products yet"));
        }
        return ResponseEntity.status(200).body(result);
    }

    // Endpoint 4 - get all products for a specific merchant
    @GetMapping("/merchant/{merchantID}")
    public ResponseEntity<?> getMerchantProducts(@PathVariable String merchantID) {
        ArrayList<String> productIDs = merchantStockService.getProductIDsByMerchant(merchantID);
        if (productIDs.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No products found for this merchant"));
        }
        ArrayList<Product> result = productService.getMerchantProducts(merchantID, productIDs);
        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("No products found for this merchant"));
        }
        return ResponseEntity.status(200).body(result);
    }

    // Endpoint 5 - add review
    @PostMapping("/review/add")
    public ResponseEntity<?> addReview(@Valid @RequestBody Review review, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = productService.addReview(review);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
        if (result == 1) return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        if (result == 2) return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
        if (result == 3) return ResponseEntity.status(400).body(new ApiResponse("Review ID already exists"));
        if (result == 4) return ResponseEntity.status(403).body(new ApiResponse("You must purchase the product before reviewing it"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add review"));
    }

    // Get all reviews for a product
    @GetMapping("/review/{productID}")
    public ResponseEntity<?> getProductReviews(@PathVariable String productID) {
        ArrayList<Review> result = productService.getProductReviews(productID);
        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("No reviews found for this product"));
        }
        return ResponseEntity.status(200).body(result);
    }

    // Endpoint 6 - apply discount
    @PutMapping("/discount/{productID}/{discountPercent}")
    public ResponseEntity<?> applyDiscount(@PathVariable String productID, @PathVariable double discountPercent) {
        int result = productService.applyDiscount(productID, discountPercent);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully"));
        if (result == 1) return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Discount must be between 1 and 99"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not apply discount"));
    }

    // Endpoint 7 - filter by price range
    @GetMapping("/priceRange/{min}/{max}")
    public ResponseEntity<?> filterByPriceRange(@PathVariable double min, @PathVariable double max) {
        ArrayList<Product> result = productService.filterByPriceRange(min, max);
        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("No products found in this price range"));
        }
        return ResponseEntity.status(200).body(result);
    }

    // Endpoint 8 - sort by price low to high
    @GetMapping("/sortByPrice")
    public ResponseEntity<?> sortByPrice() {
        ArrayList<Product> result = productService.sortByPrice();
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No products available"));
        }
        return ResponseEntity.status(200).body(result);
    }

    // Endpoint 10 - get top rated products (avg >= 4)
    @GetMapping("/topRated")
    public ArrayList<Product> getTopRated() {
        ArrayList<Product> sorted = new ArrayList<>(productService.getProducts());
        sorted.sort((p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));
        return sorted.isEmpty() ? null : sorted;
    }


    // Endpoint 11: Get full product details for everything a user has bought
    @GetMapping("/history/{userID}")
    public ResponseEntity<?> getPurchaseHistory(@PathVariable String userID) {
        ArrayList<Product> result = productService.getPurchaseHistory(userID);

        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }
        if (result.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No purchase history found for this user"));
        }
        return ResponseEntity.status(200).body(result);
    }
}