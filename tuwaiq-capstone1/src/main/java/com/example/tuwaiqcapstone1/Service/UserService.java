package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers(){
        return users;
    }

    // Returns: 0=added, 1=duplicate ID, 2=duplicate email
    public int addUsers(User user) {
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                return 1;
            }
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return 2;
            }
        }
        users.add(user);
        return 0;
    }
    public boolean updateUsers(String id, User user){
        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getId().equals(id)){
                users.set(i,user);
                return true;
            }
        }
        return false;
    }
    public boolean deleteUsers(String id){
        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getId().equals(id)){
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getByUserRole(String role){
        ArrayList<User> users1 = new ArrayList<>();
        for (int i = 0;i<users.size(); i++){
            if (users.get(i).getRole().equals(role)){
                users1.add(users.get(i));
            }
        }
        if (users1.isEmpty()){
            return null;
        }
        return users1;
    }
}
