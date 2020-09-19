package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "vote")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer voteNum;

    private Integer userId;

    private Integer rsEventId;

    // why there is no milliseconds when getting entity from db?
    private Timestamp voteTime;

    public static VoteEntity from(Vote vote) {
        return VoteEntity.builder()
                .id(vote.getId())
                .voteNum(vote.getVoteNum())
                .userId(vote.getUserId())
                .rsEventId(vote.getRsEventId())
                .voteTime(vote.getVoteTime())
                .build();
    }
}
