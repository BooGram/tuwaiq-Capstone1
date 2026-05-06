package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Model.Review;
import com.example.tuwaiqcapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReviewService {

    private final UserService userService;
    private final ProductService productService;

    ArrayList<Review> reviews = new ArrayList<>();

    public ReviewService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    // Returns: 0=added, 1=duplicate ID, 2=user not found, 3=product not found
    public int addReview(Review review) {
        for (Review r : reviews) {
            if (r.getId().equals(review.getId())) {
                return 1; // duplicate ID
            }
        }
        boolean userExists = false;
        for (User user : userService.getUsers()) {
            if (user.getId().equals(review.getUserID())) {
                userExists = true;
                // Check if user has actually purchased this product
                boolean hasPurchased = false;
                for (String pid : user.getPurchasedProductIDs()) {
                    if (pid.equals(review.getProductID())) {
                        hasPurchased = true;
                        break;
                    }
                }
                if (!hasPurchased) return 4; // user hasn't bought this product
                break;
            }
        }
        if (!userExists) return 2; // user not found

        boolean productExists = false;
        for (Product product : productService.getProducts()) {
            if (product.getId().equals(review.getProductID())) {
                productExists = true;
                break;
            }
        }
        if (!productExists) return 3; // product not found

        reviews.add(review);
        return 0;
    }

    // Get all reviews for a specific product
    public ArrayList<Review> getReviewsByProduct(String productID) {
        ArrayList<Review> result = new ArrayList<>();
        for (Review r : reviews) {
            if (r.getProductID().equals(productID)) {
                result.add(r);
            }
        }
        return result.isEmpty() ? null : result;
    }

    // Get average rating for a product
    public double getAverageRating(String productID) {
        ArrayList<Review> productReviews = getReviewsByProduct(productID);
        if (productReviews == null) return 0;
        double total = 0;
        for (Review r : productReviews) {
            total += r.getRate();
        }
        return total / productReviews.size();
    }
}