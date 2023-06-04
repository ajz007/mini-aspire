package com.miniaspire.user.service;

import com.miniaspire.user.dto.User;
import com.miniaspire.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiniAspireUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public MiniAspireUserDetailsService() {
        //default
    }

    @Autowired
    public MiniAspireUserDetailsService(UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + username));
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder
                .encode(user.getPassword()));
        userRepository.saveUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUser(String loginId) {
        return userRepository.getUser(loginId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean validate(String loginId, String password) {
        var userDetails = this.loadUserByUsername(loginId);

        return passwordEncoder.matches(password, userDetails.getPassword());
    }


}
