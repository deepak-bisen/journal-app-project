package com.ghtkdb.journal.application.service.impl;

import com.ghtkdb.journal.application.entity.User;
import com.ghtkdb.journal.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user != null){
            return org.springframework.security.core.userdetails.User.
                     builder()
                     .username(user.getUserName())
                     .password(user.getPassword())
                     .roles(user.getRole())
                     .build();
        }

     throw new UsernameNotFoundException("Username not found with username: "+ username);
    }
}
