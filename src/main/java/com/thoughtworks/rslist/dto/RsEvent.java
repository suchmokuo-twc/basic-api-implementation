package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RsEvent {

    private String eventName;
    private String keyword;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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
