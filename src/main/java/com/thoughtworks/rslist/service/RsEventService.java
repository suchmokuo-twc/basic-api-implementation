package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.InvalidUserIdException;
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
            throw new InvalidUserIdException();
        }

        RsEventEntity savedRsEventEntity = rsEventRepository.save(RsEventEntity.from(rsEvent));

        return RsEvent.from(savedRsEventEntity);
    }

    public RsEvent updateRsEvent(RsEvent rsEvent) {
        Integer id = rsEvent.getId();
        Integer userId = rsEvent.getUserId();

        if (!rsEventRepository.existsByIdAndUserId(id, userId)) {
            throw new InvalidUserIdException();
        }

        RsEventEntity updatedRsEventEntity = rsEventRepository.update(RsEventEntity.from(rsEvent));

        return RsEvent.from(updatedRsEventEntity);
    }
}
