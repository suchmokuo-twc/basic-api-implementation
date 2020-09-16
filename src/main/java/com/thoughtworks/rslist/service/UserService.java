package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final Set<User> users = new HashSet<>();

    public UserService() {
        users.add(User.builder()
                .userName("mokuo")
                .age(22)
                .gender("male")
                .phone("10000000000")
                .email("a@b.com")
                .build());
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>(users.size());
        list.addAll(users);
        return list;
    }
}
