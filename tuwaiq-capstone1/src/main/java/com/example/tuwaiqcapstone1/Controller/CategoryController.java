package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.Api.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Category;
import com.example.tuwaiqcapstone1.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.status(200).body(categoryService.getCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody Category category, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = categoryService.addCategories(category);
        if (result == 0) return ResponseEntity.status(400).body(new ApiResponse("Category ID already exists"));
        if (result == 1) return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add category"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatedCategory(@PathVariable String id, @Valid @RequestBody Category category, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (categoryService.updateCategories(id, category)) {
            return ResponseEntity.status(200).body(new ApiResponse("Category updated sucessfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {

        if (categoryService.deleteCategories(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }

}
