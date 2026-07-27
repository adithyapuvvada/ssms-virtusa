package com.ssms.userservice.config;

import com.ssms.userservice.entity.Role;
import com.ssms.userservice.repository.RoleRepository;
import com.ssms.userservice.repository.UserRepository;
import com.ssms.userservice.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostConstruct
    public void initRoles() {

        if (roleRepository.count() == 0) {

            roleRepository.save(Role.builder().name("ADMIN").build()); //manages everything
            roleRepository.save(Role.builder().name("MANAGER").build()); //manages shipment and godown
            roleRepository.save(Role.builder().name("SUPPLIER").build()); //sending and receiving goods
            roleRepository.save(Role.builder().name("ACCOUNTANT").build()); //manages billing and transactions
            roleRepository.save(Role.builder().name("INVENTORY_MANAGER").build()); //storing all details of goods
        }

        if(userRepository.count()==0){
            userService.createUser("admin","admin","ADMIN",null);
        }
    }
}