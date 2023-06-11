package com.miniaspire.user.service;

import com.miniaspire.user.dto.RegisterUser;
import com.miniaspire.user.dto.User;
import com.miniaspire.user.exception.InvalidInputException;
import com.miniaspire.user.repository.UserRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiniAspireUserDetailsService implements UserDetailsService {

    private UserRepositoryManager userRepositoryManager;

    private PasswordEncoder passwordEncoder;

    public MiniAspireUserDetailsService() {
        //default
    }

    @Autowired
    public MiniAspireUserDetailsService(UserRepositoryManager userRepositoryManager,
                                        PasswordEncoder passwordEncoder) {
        this.userRepositoryManager = userRepositoryManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositoryManager.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + username));
    }

    public void registerUser(RegisterUser registerUser) {

        if (userRepositoryManager.getUser(registerUser.getLoginId()).isPresent()) {
            throw new InvalidInputException("LoginId is not available");
        }

        registerUser.setPassword(passwordEncoder
                .encode(registerUser.getPassword()));
        try {
            userRepositoryManager.saveUser(User.from(registerUser));
        } catch (Exception e) {
            throw new RuntimeException("LoginId is not available");
        }
    }

    public List<User> getAllUsers() {
        return userRepositoryManager.getAllUsers();
    }

    public User getUser(String loginId) {
        return userRepositoryManager.getUser(loginId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean validate(String loginId, String password) {
        var userDetails = this.loadUserByUsername(loginId);

        return passwordEncoder.matches(password, userDetails.getPassword());
    }


}
