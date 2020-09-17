package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidUserException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsEventService {

    @Autowired
    private RsEventRepository rsEventRepository;

    @Autowired
    private UserRepository userRepository;

    public RsEvent createRsEvent(RsEvent rsEvent) {
        Integer userId = rsEvent.getUserId();

        if (!userRepository.existsById(userId)) {
            throw new InvalidUserException();
        }

        RsEventEntity savedRsEventEntity = rsEventRepository.save(RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(UserEntity.builder()
                        .id(userId)
                        .build())
                .build());

        return RsEvent.builder()
                .id(savedRsEventEntity.getId())
                .eventName(savedRsEventEntity.getEventName())
                .keyword(savedRsEventEntity.getKeyword())
                .userId(savedRsEventEntity.getUser().getId())
                .build();
    }
}
