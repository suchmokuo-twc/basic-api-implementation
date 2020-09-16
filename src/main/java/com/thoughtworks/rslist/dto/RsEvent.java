package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsEvent {

    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyword;

    @Valid
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

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
