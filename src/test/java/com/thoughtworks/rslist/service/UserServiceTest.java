package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void should_throw_when_delete_non_exist_user() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(100);
        });
    }
}
