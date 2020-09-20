package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    @Builder.Default
    private Integer votes = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<RsEventEntity> rsEvents;

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .votes(user.getVotes())
                .build();
    }
}
