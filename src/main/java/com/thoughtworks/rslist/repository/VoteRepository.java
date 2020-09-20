package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {

    List<VoteEntity> findAllByVoteTimeBetween(Timestamp startTime, Timestamp endTime);
}
