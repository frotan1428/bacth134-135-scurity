package com.tpe.security.service;


import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsImpl implements UserDetailsService {

    //in this class we are going  to convert
    //1. User Entity to UserDetails
    //2 Role Entity to Granted Authority

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//ali123

        //using username find user from DB
       User foundUser= userRepository.findByUserName(username).orElseThrow(
                ()-> new ResourceNotFoundException("User Not found By Name"+ username)//ali123
        );
       if (foundUser !=null){
           return  new org.
                   springframework.
                   security.
                   core.
                   userdetails.User(
                            foundUser.getUserName(),
                            foundUser.getPassword(),
                            buildGrantedAuthorities(foundUser.getRoles())
           );
       }else{
           throw  new UsernameNotFoundException("user not found with name "+ username);
       }

    }
    //
    private static List<SimpleGrantedAuthority> buildGrantedAuthorities(final Set<Role> roles){

        List<SimpleGrantedAuthority>  authorities = new ArrayList<>();

        for (Role role:roles){
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return authorities;

    }


}
