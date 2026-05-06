package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {


    ArrayList<Category> categories = new ArrayList<>();

    public ArrayList<Category> getCategories(){
        return categories;
    }
    public int addCategories(Category category) {
        for (Category c : categories) {
            if (c.getId().equals(category.getId())) {
                return 0;
            }
        }
        categories.add(category);
        return 1;
    }
    public boolean updateCategories(String id, Category category){
        for (int i = 0; i<categories.size(); i++){
            if (categories.get(i).getId().equals(id)){
                categories.set(i,category);
                return true;
            }
        }
        return false;
    }
    public boolean deleteCategories(String id){
        for (int i = 0; i<categories.size(); i++){
            if (categories.get(i).getId().equals(id)){
                categories.remove(i);
                return true;
            }
        }
        return false;
    }

}
