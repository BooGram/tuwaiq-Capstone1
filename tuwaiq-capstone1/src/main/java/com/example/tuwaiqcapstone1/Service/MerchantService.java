package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants = new ArrayList<>();

    public ArrayList<Merchant> getMerchants(){
        return merchants;
    }

    // Returns: 0=added, 1=duplicate ID
    public int addMerchants(Merchant merchant) {
        for (Merchant m : merchants) {
            if (m.getId().equals(merchant.getId())) {
                return 1; // duplicate ID
            }
        }
        merchants.add(merchant);
        return 0;
    }

    public boolean updateMerchants(String id, Merchant merchant){
        for (int i = 0; i<merchants.size(); i++){
            if (merchants.get(i).getId().equals(id)){
                merchants.set(i,merchant);
                return true;
            }
        }
        return false;
    }
    public boolean deleteMerchants(String id){
        for (int i = 0; i<merchants.size(); i++){
            if (merchants.get(i).getId().equals(id)){
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }
}
