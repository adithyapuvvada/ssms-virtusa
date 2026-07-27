package com.ssms.userservice.service;

import com.ssms.userservice.entity.Role;
import com.ssms.userservice.entity.User;
import com.ssms.userservice.exception.UserException;
import com.ssms.userservice.repository.RoleRepository;
import com.ssms.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Create new user
    public User createUser(String username, String password, String roleName,Long companyId) {

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new UserException("Role not found!. please select this users only " +
                        "ADMIN, MANAGER, SUPPLIER, INVENTORY_MANAGER, ACCOUNTANT"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        if(roleName.equalsIgnoreCase("supplier")){
            if(companyId == null){
                throw new UserException("Supplier must have a company id");
            }
            user.setCompanyId(companyId);
        }
        else{
            user.setCompanyId(null);
        }

        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> userList= userRepository.findAll();
        if(userList.isEmpty()){
            throw new UserException("No users Exist");
        }
        return userList;
    }

    //get by id
    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserException("user with "+id+" not found"));
    }

    // Delete user
    public void deleteUser(Long id) {
        User user = findById(id);
        if(user.getRole().getName().equals("ADMIN")){
            throw new UserException("ADMIN cannot be deleted");
        }
        userRepository.deleteById(id);
    }
}