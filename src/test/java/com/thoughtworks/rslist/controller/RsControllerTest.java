package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;

import static com.thoughtworks.rslist.utils.EntityUtil.createDemoRsEventEntity;
import static com.thoughtworks.rslist.utils.EntityUtil.createDemoUserEntity;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @Test
    void should_get_all_rs_events() throws Exception {
        int eventsNum = 3;

        for (int i = 0; i < eventsNum; i++) {
            UserEntity user = userRepository.save(createDemoUserEntity());
            rsEventRepository.save(createDemoRsEventEntity(user));
        }

        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(eventsNum)));
    }

    @Test
    void should_get_all_rs_events_without_user() throws Exception {
        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user").doesNotExist());
    }
    
    @Test
    void should_update_rs_event() throws Exception {
        UserEntity demoUserEntity = userRepository.save(createDemoUserEntity());
        RsEventEntity rsEventEntity = rsEventRepository.save(createDemoRsEventEntity(demoUserEntity));

        String rsEventWithNewKeywordJson = RsEvent.builder()
                .keyword("分类1")
                .userId(demoUserEntity.getId())
                .build()
                .toJson();

        Integer rsEventId = rsEventEntity.getId();

        mockMvc.perform(patch("/rs/events/" + rsEventId)
                .content(rsEventWithNewKeywordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventEntity updatedRsEventEntity = rsEventRepository.findById(rsEventId).get();

        assertEquals(updatedRsEventEntity.getEventName(), rsEventEntity.getEventName());
        assertEquals(updatedRsEventEntity.getKeyword(), "分类1");
    }

    @Test
    void should_not_update_rs_event_when_invalid_userId() throws Exception {
        UserEntity demoUserEntity = userRepository.save(createDemoUserEntity());
        RsEventEntity rsEventEntity = rsEventRepository.save(createDemoRsEventEntity(demoUserEntity));

        String rsEventWithNewKeywordJson = RsEvent.builder()
                .keyword("分类1")
                .userId(demoUserEntity.getId() + 1)
                .build()
                .toJson();

        Integer rsEventId = rsEventEntity.getId();

        mockMvc.perform(patch("/rs/events/" + rsEventId)
                .content(rsEventWithNewKeywordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_delete_rs_event() throws Exception {
        UserEntity user = userRepository.save(createDemoUserEntity());
        rsEventRepository.save(createDemoRsEventEntity(user));

        mockMvc.perform(delete("/rs/events/1"))
                .andExpect(status().isOk());
    }

    @Test
    void should_create_rs_event() throws Exception {
        UserEntity userEntity = userRepository.save(createDemoUserEntity());

        Integer userId = userEntity.getId();

        String newRsEventJson = RsEvent.builder()
                .eventName("新事件")
                .keyword("key1")
                .userId(userId)
                .build()
                .toJson();

        mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is("/rs/events/1")));
    }

    @Test
    void should_not_create_rs_event_when_invalid_userId() throws Exception {
        Integer userId = 123;

        String newRsEventJson = RsEvent.builder()
                .eventName("新事件")
                .keyword("key1")
                .userId(userId)
                .build()
                .toJson();

        mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_create_rs_event_when_empty_eventName() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName(null)
                .keyword("key1")
                .userId(1)
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_keyword() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword(null)
                .userId(1)
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_userId() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .userId(null)
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_get_event_error_when_out_of_range() throws Exception {
        mockMvc.perform(get("/rs/events?start=1&end=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_get_event_error_when_invalid_id() throws Exception {
        mockMvc.perform(get("/rs/events/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("no such event")));
    }

    @Test
    void should_create_rs_event_error_when_invalid_param() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .build()
                .toJson();

        mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_vote_success() throws Exception {
        int voteNum = 5;
        int currentRsEventVotes = 100;
        int currentUserVotes = 10;

        UserEntity demoUserEntity = createDemoUserEntity();
        demoUserEntity.setVotes(currentUserVotes);
        UserEntity userEntity = userRepository.save(demoUserEntity);

        RsEventEntity demoRsEventEntity = createDemoRsEventEntity(userEntity);
        demoRsEventEntity.setVotes(currentRsEventVotes);
        RsEventEntity rsEventEntity = rsEventRepository.save(demoRsEventEntity);

        Timestamp voteTime = getCurrentTime();

        String voteJson = Vote.builder()
                .voteNum(voteNum)
                .voteTime(voteTime)
                .userId(userEntity.getId())
                .build()
                .toJson();

        mockMvc.perform(post("/rs/votes/" + rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(voteJson))
                .andExpect(status().isOk());

        VoteEntity voteEntity = voteRepository.findById(1).get();

        assertEquals(voteEntity.getUserId(), userEntity.getId());
        assertEquals(voteEntity.getVoteNum(), voteNum);
        assertEquals(voteEntity.getVoteTime(), voteTime);

        userEntity = userRepository.findById(userEntity.getId()).get();
        rsEventEntity = rsEventRepository.findById(rsEventEntity.getId()).get();

        int userRemainVotes = currentUserVotes - voteNum;
        int votedRsEventVotes = currentRsEventVotes + voteNum;

        assertEquals(userEntity.getVotes(), userRemainVotes);
        assertEquals(rsEventEntity.getVotes(), votedRsEventVotes);
    }

    @Test
    void should_vote_fail_when_no_enough_votes() throws Exception {
        int voteNum = 5;
        int currentRsEventVotes = 100;
        int currentUserVotes = 4;

        UserEntity demoUserEntity = createDemoUserEntity();
        demoUserEntity.setVotes(currentUserVotes);
        UserEntity userEntity = userRepository.save(demoUserEntity);

        RsEventEntity demoRsEventEntity = createDemoRsEventEntity(userEntity);
        demoRsEventEntity.setVotes(currentRsEventVotes);
        RsEventEntity rsEventEntity = rsEventRepository.save(demoRsEventEntity);

        Timestamp voteTime = getCurrentTime();

        String voteJson = Vote.builder()
                .voteNum(voteNum)
                .voteTime(voteTime)
                .userId(userEntity.getId())
                .build()
                .toJson();

        mockMvc.perform(post("/rs/votes/" + rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(voteJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("user votes not enough")));
    }

    private Timestamp getCurrentTime() {
        // remove milliseconds.
        long timestamp = (System.currentTimeMillis() / 1000) * 1000;

        return new Timestamp(timestamp);
    }
}
