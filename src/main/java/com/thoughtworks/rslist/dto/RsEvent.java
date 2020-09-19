package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.RsEventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsEvent extends BaseDto {

    public interface WithoutUserView {}
    public interface WithUserView extends WithoutUserView {}

    @JsonView(WithoutUserView.class)
    private Integer id;

    @NotEmpty
    @JsonView(WithoutUserView.class)
    private String eventName;

    @NotEmpty
    @JsonView(WithoutUserView.class)
    private String keyword;

    @Valid
    @NotNull
    @JsonView(WithUserView.class)
    private Integer userId;

    @JsonView(WithoutUserView.class)
    private Integer votes;

    public RsEvent merge(RsEvent rsEvent) {
        if (rsEvent.eventName != null) {
            this.eventName = rsEvent.eventName;
        }

        if (rsEvent.keyword != null) {
            this.keyword = rsEvent.keyword;
        }

        return this;
    }

    @Override
    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper
                .writerWithView(WithUserView.class)
                .writeValueAsString(this);
    }

    public static RsEvent from(RsEventEntity rsEventEntity) {
        return RsEvent.builder()
                .id(rsEventEntity.getId())
                .eventName(rsEventEntity.getEventName())
                .keyword(rsEventEntity.getKeyword())
                .userId(rsEventEntity.getUser().getId())
                .votes(rsEventEntity.getVotes())
                .build();
    }
}
