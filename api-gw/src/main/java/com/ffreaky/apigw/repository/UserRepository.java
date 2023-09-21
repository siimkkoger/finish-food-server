package com.ffreaky.apigw.repository;

import com.ffreaky.apigw.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    UserEntity findByPhoneNumber(String phoneNumber);
}
