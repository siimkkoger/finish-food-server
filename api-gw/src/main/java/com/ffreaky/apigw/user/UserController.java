package com.ffreaky.apigw.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
                userEntity.getPhoneNumber(),
                userEntity.getAddress().stream().map(this::convertToUserAddressDTORecord).collect(Collectors.toSet()));
    }

    UserAddressDTORecord convertToUserAddressDTORecord(UserAddressEntity userAddressEntity) {
        return new UserAddressDTORecord(
                userAddressEntity.getId(),
                userAddressEntity.getStreetAddress(),
                userAddressEntity.getCity(),
                userAddressEntity.getState(),
                userAddressEntity.getPostalIndex(),
                userAddressEntity.getCountry(),
                userAddressEntity.getPhoneNumber(),
                userAddressEntity.getMobile()
        );
    }
}
