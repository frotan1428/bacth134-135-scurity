package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.domain.enums.UserRole;
import com.tpe.dto.UserRequest;
import com.tpe.exception.ConflictException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;


    public void save(UserRequest userRequest) {

        if (userRepository.existsByUserName(userRequest.getUserName())){
            throw new ConflictException("User name is already exist in db");
        }

        User newUser = new User();
        newUser.setFirstName(userRequest.getFirstName());
        newUser.setLastName(userRequest.getLastName());
        newUser.setUserName(userRequest.getUserName());

        //encode password then we should pass in setPassword()
       // newUser.setPassword(userRequest.getPassword());//1234

        String password= userRequest.getPassword();//1234

        String encodePassword = passwordEncoder.encode(password);
        newUser.setPassword(encodePassword);

         Role role= roleService.getRoleType(UserRole.ROLE_ADMIN);//get role type from if there is no role get exception

        Set<Role> roles= new HashSet<>();
        roles.add(role);

        newUser.setRoles(roles);

        userRepository.save(newUser);



    }
}
