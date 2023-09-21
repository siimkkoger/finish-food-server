package com.ffreaky.apigw.user;

import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_address", schema = "public")
public class UserAddressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postal_index", nullable = false)
    private String postalIndex;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "mobile", nullable = false)
    private String mobile;
}
