package com.ffreaky.apigw.controller;

import com.ffreaky.apigw.entities.UserEntity;
import com.ffreaky.apigw.model.UserDTORecord;
import com.ffreaky.apigw.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api-gw/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/getall")
    List<UserDTORecord> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToUserDTORecord).toList();
    }

    UserDTORecord convertToUserDTORecord(UserEntity userEntity) {
        return new UserDTORecord(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPhoneNumber()
        );
    }
}
