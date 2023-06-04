package com.miniaspire.user.repository;

import com.miniaspire.user.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByLoginId(String loginId);
}
