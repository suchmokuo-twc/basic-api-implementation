package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.entity.VoteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Vote extends BaseDto {

    private Integer id;

    @NotNull
    @Min(0)
    private Integer voteNum;

    @NotNull
    private Integer userId;

    private Integer rsEventId;

    @NotNull
    private Timestamp voteTime;

    public static Vote from(VoteEntity voteEntity) {
        return Vote.builder()
                .id(voteEntity.getId())
                .voteNum(voteEntity.getVoteNum())
                .userId(voteEntity.getUserId())
                .rsEventId(voteEntity.getRsEventId())
                .voteTime(voteEntity.getVoteTime())
                .build();
    }
}
