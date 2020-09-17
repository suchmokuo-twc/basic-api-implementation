package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import static com.thoughtworks.rslist.utils.EntityUtil.createDemoRsEventEntity;
import static com.thoughtworks.rslist.utils.EntityUtil.createDemoUserEntity;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_get_all_rs_events() throws Exception {
        mockMvc.perform(get("/rs/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
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
        String deletedRsEventJson = RsEvent.builder()
                .eventName("第一条事件")
                .keyword("无分类")
                .build()
                .toJson();

        mockMvc.perform(delete("/rs/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(deletedRsEventJson));
    }

    @Test
    void should_create_rs_event() throws Exception {
        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .userName("name")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("10000000000")
                .build());

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
    void should_get_event_error_when_invalid_index() throws Exception {
        mockMvc.perform(get("/rs/events/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
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
}
