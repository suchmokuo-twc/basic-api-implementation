package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.UserNotFoundException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        UserEntity savedUserEntity = userRepository.save(UserEntity.from(user));

        return User.from(savedUserEntity);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::from)
                .collect(Collectors.toList());
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .map(User::from)
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
