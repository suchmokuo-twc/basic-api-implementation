package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.dto.User;

public class DtoUtil {

    public static User createDemoUser() {
        return User.builder()
                .userName("name")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("10000000000")
                .build();
    }
}
