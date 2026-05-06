package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Merchant;
import com.example.tuwaiqcapstone1.Model.MerchantStock;
import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantStockService {
    private final MerchantService merchantService;
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    private final ProductService productService;
    private final UserService userService;

    public MerchantStockService(ProductService productService, UserService userService, MerchantService merchantService) {
        this.productService = productService;
        this.userService = userService;
        this.merchantService = merchantService;
    }

    public ArrayList<MerchantStock> getMerchantStocks() {

        return merchantStocks;
    }
    // Returns: 0=added, 1=duplicate ID, 2=product not found, 3=merchant not found
    public int addMerchantStocks(MerchantStock merchantStock) {
        for (MerchantStock ms : merchantStocks) {
            if (ms.getId().equals(merchantStock.getId())) {
                return 1; // duplicate ID
            }
        }
        boolean productExists = false;
        for (Product product : productService.getProducts()) {
            if (product.getId().equals(merchantStock.getProductID())) {
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            return 2; // product not found
        }
        boolean merchantExists = false;
        for (Merchant merchant : merchantService.getMerchants()) {
            if (merchant.getId().equals(merchantStock.getMerchantID())) {
                merchantExists = true;
                break;
            }
        }
        if (!merchantExists) {
            return 3; // merchant not found
        }
        merchantStocks.add(merchantStock);
        return 0;
    }
    public boolean updateMerchantStocks(String id, MerchantStock merchantStock) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.set(i, merchantStock);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchantStocks(String id) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }

    public int increaseStock(String productID, String merchantID, int stocks) {
        if (stocks <= 0) {
            return 2; // stocks must be positive
        }
        for (MerchantStock currentStock : merchantStocks) {
            if (currentStock.getProductID().equals(productID) &&
                    currentStock.getMerchantID().equals(merchantID)) {
                currentStock.setStock(currentStock.getStock() + stocks);
                return 0;
            }
        }
        return 1; // stock entry not found
    }

    public String buyProduct(String userID, String productID, String merchantID) {

        User foundUser = null;
        for (User user : userService.getUsers()) {
            if (user.getId().equals(userID)) {
                foundUser = user;
                break;
            }
        }
        if (foundUser == null) {
            return "User not found";
        }

        Product foundProduct = null;
        for (Product product : productService.getProducts()) {
            if (product.getId().equals(productID)) {
                foundProduct = product;
                break;
            }
        }
        if (foundProduct == null) {
            return "Product not found";
        }

        MerchantStock foundStock = null;
        for (MerchantStock ms : merchantStocks) {
            if (ms.getProductID().equals(productID) &&
                    ms.getMerchantID().equals(merchantID)) {
                foundStock = ms;
                break;
            }
        }
        if (foundStock == null) {
            return "Merchant does not carry this product";
        }
        if (foundStock.getStock() <= 0) {
            return "Product is out of stock";
        }

        double effectivePrice = (foundProduct.getDiscount() > 0)
                ? foundProduct.getPriceAfterDiscount()
                : foundProduct.getPrice();

        if (foundUser.getBalance() < effectivePrice) {
            return "Insufficient balance";
        }

        foundStock.setStock(foundStock.getStock() - 1);
        foundUser.setBalance(foundUser.getBalance() - effectivePrice);

        if (!foundUser.getPurchasedProductIDs().contains(productID)) {
            foundUser.getPurchasedProductIDs().add(productID);
        }

        return "Purchase successful";
    }
    // Endpoint 4 - get all productIDs for a merchant (used by ProductService.getMerchantProducts)
    public ArrayList<String> getProductIDsByMerchant(String merchantID) {
        ArrayList<String> productIDs = new ArrayList<>();
        for (MerchantStock ms : merchantStocks) {
            if (ms.getMerchantID().equals(merchantID)) {
                productIDs.add(ms.getProductID());
            }
        }
        return productIDs;
    }

    // Endpoint 9 - get total stock count for a merchant
    // Returns: -1 if merchant not found in stocks, otherwise total
    public int getMerchantTotalStock(String merchantID) {
        int total = 0;
        boolean found = false;
        for (MerchantStock ms : merchantStocks) {
            if (ms.getMerchantID().equals(merchantID)) {
                total += ms.getStock();
                found = true;
            }
        }
        if (!found) {
            return -1;
        }
        return total;
    }
}