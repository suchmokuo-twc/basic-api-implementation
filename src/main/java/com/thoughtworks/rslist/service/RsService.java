package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidUserIdException;
import com.thoughtworks.rslist.exception.RsEventNotFoundException;
import com.thoughtworks.rslist.exception.UserNotFoundException;
import com.thoughtworks.rslist.exception.UserVotesNotEnoughException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class RsService {

    private final RsEventRepository rsEventRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public RsService(RsEventRepository rsEventRepository,
                     UserRepository userRepository,
                     VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

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

        RsEventEntity rsEventEntity = RsEventEntity.from(rsEvent);
        RsEventEntity oldRsEventEntity = rsEventRepository.findById(rsEventEntity.getId())
                .orElseThrow(RsEventNotFoundException::new);

        RsEventEntity updatedRsEventEntity = oldRsEventEntity.merge(rsEventEntity);

        rsEventRepository.save(updatedRsEventEntity);

        return RsEvent.from(updatedRsEventEntity);
    }

    public Vote createVote(Vote vote) {
        Integer rsEventId = vote.getRsEventId();

        RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventId)
                .orElseThrow(RsEventNotFoundException::new);

        Integer userId = vote.getUserId();

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Integer voteNum = vote.getVoteNum();
        Integer userRemainVotes = userEntity.getVotes();
        int userRemainVotesAfterVoting = userRemainVotes - voteNum;

        if (userRemainVotesAfterVoting < 0) {
            throw new UserVotesNotEnoughException();
        }

        rsEventEntity.setVotes(rsEventEntity.getVotes() + voteNum);
        userEntity.setVotes(userRemainVotesAfterVoting);

        rsEventRepository.save(rsEventEntity);
        userRepository.save(userEntity);

        VoteEntity voteEntity = voteRepository.save(VoteEntity.from(vote));

        return Vote.from(voteEntity);
    }

    public List<RsEvent> getAllRsEvents() {
        return rsEventRepository.findAll()
                .stream()
                .map(RsEvent::from)
                .collect(Collectors.toList());
    }

    public RsEvent getRsEventById(int id) {
        return rsEventRepository.findById(id)
                .map(RsEvent::from)
                .orElseThrow(RsEventNotFoundException::new);
    }

    public void deleteRsEventById(int id) {
        if (!rsEventRepository.existsById(id)) {
            throw new RsEventNotFoundException();
        }

        rsEventRepository.deleteById(id);
    }

    public List<Vote> getVotes(Timestamp startTime, Timestamp endTime) {
        return voteRepository.findAllByVoteTime(startTime, endTime)
                .stream()
                .map(Vote::from)
                .collect(Collectors.toList());
    }
}
