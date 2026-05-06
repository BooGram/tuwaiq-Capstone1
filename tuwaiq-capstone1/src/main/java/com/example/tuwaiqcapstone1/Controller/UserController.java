package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.Api.ApiResponse;
import com.example.tuwaiqcapstone1.Model.User;
import com.example.tuwaiqcapstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        int result = userService.addUsers(user);
        if (result == 0) return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("User ID already exists"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Email already registered"));
        return ResponseEntity.status(400).body(new ApiResponse("Could not add user"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatedCategory(@PathVariable String id, @Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        if (userService.updateUsers(id, user)) {
            return ResponseEntity.status(200).body(new ApiResponse("User updated sucessfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {

        if (userService.deleteUsers(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
    }
    // Endpoint 1 - get users by role
    @GetMapping("/getByRole/{role}")
    public ResponseEntity<?> getByRole(@PathVariable String role) {
        ArrayList<User> result = userService.getByUserRole(role);
        if (result == null) {
            return ResponseEntity.status(404).body(new ApiResponse("No users found with role: " + role));
        }
        return ResponseEntity.status(200).body(result);
    }
}