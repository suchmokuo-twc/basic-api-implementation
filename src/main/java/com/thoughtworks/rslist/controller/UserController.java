package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.InvalidUserException;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=utf-8")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User registeredUser = userService.register(user);

        Integer id = registeredUser.getId();

        return ResponseEntity
                .created(URI.create("/users/" + id))
                .body(registeredUser);
    }

    @GetMapping("/error")
    public ResponseEntity<String> getError() {
        throw new InvalidUserException();
    }
}
