package com.ssms.userservice.controller;

import com.ssms.userservice.dto.CreateUserRequest;
import com.ssms.userservice.entity.User;
import com.ssms.userservice.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssms/userservice/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

   @PostMapping
   public User createUser(@RequestHeader(value="X-User-Role", required=false) String role,
                        @RequestBody CreateUserRequest request){
       return userService.createUser(
               request.getUsername(),
               request.getPassword(),
               request.getRole(),
               request.getCompanyId()
       );
   }

    @GetMapping
    public List<User> getAllUsers(@RequestHeader(value="X-User-Role", required=false) String role) {
        return userService.getAllUsers();
    }



    @DeleteMapping("/{id}")
    public String deleteUser(@RequestHeader(value="X-User-Role", required=false) String role,@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}