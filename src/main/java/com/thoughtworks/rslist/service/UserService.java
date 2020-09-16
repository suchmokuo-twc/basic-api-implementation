package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final Set<User> users = new HashSet<>();

    public void registerUser(User user) {
        users.add(user);
    }
}
