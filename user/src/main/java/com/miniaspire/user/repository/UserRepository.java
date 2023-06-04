package com.miniaspire.user.repository;

import com.miniaspire.user.dto.MiniAspireUserAuthDetails;
import com.miniaspire.user.dto.User;
import com.miniaspire.user.dto.UserRole;
import com.miniaspire.user.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final IUserRepository repository;

    @Autowired
    public UserRepository(IUserRepository userRepository) {
        this.repository = userRepository;
    }

    public Optional<MiniAspireUserAuthDetails> findByLoginId(String loginId) {
        return repository.findByLoginId(loginId).map(userEntity -> {
            var user = new MiniAspireUserAuthDetails();
            user.setUsername(userEntity.getLoginId());
            user.setPassword(userEntity.getPassword());
            return user;
        });
    }

    public Optional<User> getUser(String loginId) {
        return repository.findByLoginId(loginId).map(userEntity -> {
            var user = new User();
            user.setUsername(userEntity.getName());
            user.setLoginId(userEntity.getLoginId());
            user.setUserRole(UserRole.fromValue(userEntity.getRole()));
            user.setEmail(userEntity.getEmail());
            return user;
        });
    }

    public void saveUser(User user) {
        var entity = new UserEntity();
        entity.setLoginId(user.getLoginId());
        entity.setPassword(user.getPassword());
        entity.setName(user.getUsername());
        entity.setRole(user.getUserRole().getValue());
        repository.save(entity);
    }

    public List<User> getAllUsers() {
        var userList = new ArrayList<User>();

        for (UserEntity userEntity : repository.findAll()) {
            var user = new User();
            user.setUsername(userEntity.getName());
            user.setLoginId(userEntity.getLoginId());
            user.setUserRole(UserRole.fromValue(userEntity.getRole()));
            user.setEmail(userEntity.getEmail());
            userList.add(user);
        }
        return userList;
    }


}
