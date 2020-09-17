package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @NotEmpty
    @JsonView(WithoutUserView.class)
    private String eventName;

    @NotEmpty
    @JsonView(WithoutUserView.class)
    private String keyword;

    @Valid
    @NotNull
    @JsonView(WithUserView.class)
    private User user;

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
}
