package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.UserNotFoundException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final Set<User> users = new HashSet<>();

    @Autowired
    private UserRepository userRepository;

    public UserService() {
        users.add(User.builder()
                .userName("mokuo")
                .age(22)
                .gender("male")
                .phone("10000000000")
                .email("a@b.com")
                .build());
    }

    // old api
    public User registerUser(User user) {
        users.add(user);
        return user;
    }

    public User register(User user) {
        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return User.builder()
                .id(savedUserEntity.getId())
                .userName(savedUserEntity.getUserName())
                .gender(savedUserEntity.getGender())
                .email(savedUserEntity.getEmail())
                .phone(savedUserEntity.getPhone())
                .age(savedUserEntity.getAge())
                .build();
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>(users.size());
        list.addAll(users);
        return list;
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .map(userEntity -> User.builder()
                        .id(userEntity.getId())
                        .userName(userEntity.getUserName())
                        .age(userEntity.getAge())
                        .email(userEntity.getEmail())
                        .phone(userEntity.getPhone())
                        .gender(userEntity.getGender())
                        .build())
                .orElseThrow(UserNotFoundException::new);
    }

    public void deleteUserById(int id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException();
        }
    }
}
