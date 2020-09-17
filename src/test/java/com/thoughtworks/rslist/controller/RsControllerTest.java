package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        String rsEventWithNewKeywordJson = RsEvent.builder()
                .keyword("分类1")
                .build()
                .toJson();

        mockMvc.perform(put("/rs/events/1")
                .content(rsEventWithNewKeywordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyword", is("分类1")))
                .andExpect(jsonPath("$.eventName", is("第一条事件")));

        String rsEventWithNewNameJson = RsEvent.builder()
                .eventName("new name")
                .build()
                .toJson();

        mockMvc.perform(put("/rs/events/1")
                .content(rsEventWithNewNameJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyword", is("分类1")))
                .andExpect(jsonPath("$.eventName", is("new name")));
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
        String newRsEventJson = RsEvent.builder()
                .eventName("新事件")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is("/rs/events/4")));
    }

    @Test
    void should_not_create_rs_event_when_empty_eventName() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName(null)
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
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
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_user() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(null)
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_userName() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName(null)
                        .age(20)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_userName_is_longer_than_8() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("123456789")
                        .age(20)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_user_gender() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender(null)
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_user_age() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(null)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_invalid_user_age() throws Exception {
        String rsEventWithUserAge17Json = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(17)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(rsEventWithUserAge17Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());

        String rsEventWithUserAge101Json = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(101)
                        .gender("male")
                        .email("abc@twc.com")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        mvcResult = mockMvc.perform(post("/rs/events")
                .content(rsEventWithUserAge101Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_empty_user_email() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender("male")
                        .email(null)
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_invalid_user_email() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender("male")
                        .email("abc")
                        .phone("10000000000")
                        .build())
                .build()
                .toJson();

        MvcResult mvcResult = mockMvc.perform(post("/rs/events")
                .content(newRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), OK.value());
    }

    @Test
    void should_not_create_rs_event_when_invalid_user_phone() throws Exception {
        String newRsEventJson = RsEvent.builder()
                .eventName("name")
                .keyword("key1")
                .user(User.builder()
                        .userName("abc")
                        .age(20)
                        .gender("male")
                        .email("abc")
                        .phone("20000000000")
                        .build())
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
