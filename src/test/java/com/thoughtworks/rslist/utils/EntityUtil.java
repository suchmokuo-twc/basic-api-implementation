package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;

public class EntityUtil {

    public static UserEntity createDemoUserEntity() {
        return UserEntity.builder()
                .userName("name")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("10000000000")
                .build();
    }

    public static RsEventEntity createDemoRsEventEntity(UserEntity userEntity) {
        return RsEventEntity.builder()
                .eventName("事件")
                .keyword("关键")
                .user(userEntity)
                .build();
    }
}
