package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Category;
import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Model.Review;
import com.example.tuwaiqcapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {

    private final UserService userService;
    private final CategoryService categoryService;

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Review> reviews = new ArrayList<>();

    public ProductService(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    // Returns: 0=added, 1=duplicate ID, 2=category not found
    public int addProducts(Product product) {
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                return 1;
            }
        }
        boolean categoryExists = false;
        for (Category category : categoryService.getCategories()) {
            if (category.getId().equals(product.getCategoryID())) {
                categoryExists = true;
                break;
            }
        }
        if (!categoryExists) {
            return 2;
        }
        products.add(product);
        return 0;
    }

    public boolean updateProducts(String id, Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.set(i, product);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProducts(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    // Endpoint 2 - recommendation based on user balance
    public ArrayList<Product> recommendations(String userID) {
        ArrayList<Product> recommended = new ArrayList<>();
        User foundUser = null;
        for (User user : userService.getUsers()) {
            if (user.getId().equals(userID)) {
                foundUser = user;
                break;
            }
        }
        if (foundUser == null) {
            return null;
        }
        for (Product product : products) {
            if (product.getPrice() <= foundUser.getBalance()) {
                recommended.add(product);
            }
        }
        return recommended;
    }

    // Endpoint 3 - filter by category
    public ArrayList<Product> filterCategory(String categoryID) {

        // First: confirm the category actually exists
        boolean categoryExists = false;
        for (Category category : categoryService.getCategories()) {
            if (category.getId().equals(categoryID)) {
                categoryExists = true;
                break;
            }
        }
        if (!categoryExists) {
            return null; // signals "category not found"
        }

        // Second: collect matching products
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategoryID().equals(categoryID)) {
                result.add(product);
            }
        }
        return result; // may be empty — category exists but has no products yet
    }

    // Endpoint 4 - get all products that belong to a specific merchant (via merchantStocks)
    public ArrayList<Product> getMerchantProducts(String merchantID, ArrayList<String> productIDs) {
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (productIDs.contains(product.getId())) {
                result.add(product);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    // Endpoint 5 - add review
    // Returns: 0=added, 1=user not found, 2=product not found, 3=duplicate review ID
    public int addReview(Review review) {
        for (Review r : reviews) {
            if (r.getId().equals(review.getId())) {
                return 3; // duplicate
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
        if (!userExists) return 1;

        Product targetProduct = null;
        for (Product product : products) {
            if (product.getId().equals(review.getProductID())) {
                targetProduct = product;
                break;
            }
        }
        if (targetProduct == null) return 2;

        reviews.add(review);

        // Recalculate average rating for this product
        double total = 0;
        int count = 0;
        for (Review r : reviews) {
            if (r.getProductID().equals(targetProduct.getId())) {
                total += r.getRate();
                count++;
            }
        }
        targetProduct.setRating(total / count); // updates the product's rating live

        return 0;
    }

    // Get all reviews for a product
    public ArrayList<Review> getProductReviews(String productID) {
        ArrayList<Review> result = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getProductID().equals(productID)) {
                result.add(review);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    // Endpoint 6 - apply discount on a product
    // Returns: 0=success, 1=product not found, 2=invalid discount
    public int applyDiscount(String productID, double discountPercent) {
        if (discountPercent <= 0 || discountPercent >= 100) {
            return 2;
        }
        for (Product product : products) {
            if (product.getId().equals(productID)) {
                product.setDiscount(discountPercent);
                // Always compute from original price — no double-discount possible
                product.setPriceAfterDiscount(product.getPrice() * (1 - discountPercent / 100));
                return 0;
            }
        }
        return 1;
    }

    // Endpoint 7 - filter by price range
    public ArrayList<Product> filterByPriceRange(double min, double max) {
        if (min > max) {
            return null;
        }
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getPrice() >= min && product.getPrice() <= max) {
                result.add(product);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    // Endpoint 8 - sort by price low to high
    public ArrayList<Product> sortByPrice() {
        ArrayList<Product> sorted = new ArrayList<>(products);
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - i - 1; j++) {
                if (sorted.get(j).getPrice() > sorted.get(j + 1).getPrice()) {
                    Product temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }


    // Endpoint 10 - get top rated products
    public ArrayList<Product> getTopRated() {

        // 1. Create a tiny local helper class
        class ProductWithAvg {
            Product product;
            double avg;

            ProductWithAvg(Product product, double avg) {
                this.product = product;
                this.avg = avg;
            }
        }

        ArrayList<ProductWithAvg> tempList = new ArrayList<>();

        // 2. Calculate averages and store them in the wrapper objects
        for (Product product : products) {
            double total = 0;
            int count = 0;

            for (Review review : reviews) {
                if (review.getProductID().equals(product.getId())) {
                    total += review.getRate();
                    count++;
                }
            }

            double avg = (count == 0) ? 0 : total / count;
            tempList.add(new ProductWithAvg(product, avg));
        }

        tempList.sort((p1, p2) -> Double.compare(p2.avg, p1.avg));

        ArrayList<Product> result = new ArrayList<>();
        for (ProductWithAvg item : tempList) {
            result.add(item.product);
        }

        return result;
    }
    // Endpoint 11 - get purchase history

    public ArrayList<Product> getPurchaseHistory(String userID) {
        User foundUser = null;
        // Find the user to get their purchased IDs
        for (User user : userService.getUsers()) {
            if (user.getId().equals(userID)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            return null; // Signals user not found
        }

        ArrayList<Product> history = new ArrayList<>();
        // Iterate through the user's purchased IDs and find the matching products
        for (String pid : foundUser.getPurchasedProductIDs()) {
            for (Product product : products) {
                if (product.getId().equals(pid)) {
                    history.add(product);
                    break;
                }
            }
        }
        return history;
    }
}