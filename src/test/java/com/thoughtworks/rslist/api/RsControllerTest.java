package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_throw_if_index_out_of_range() {

    }

    @Test
    void should_update_rs_event() throws Exception {
        String updatedRsEventJson = stringify(new RsEvent("第一条事件", "分类1"));

        mockMvc.perform(put("/rs/event/1")
                .content(updatedRsEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(updatedRsEventJson));
    }

    @Test
    void should_delete_rs_event() throws Exception {
        String deletedRsEventJson = stringify(new RsEvent("第一条事件", "无分类"));

        mockMvc.perform(delete("/rs/event/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(deletedRsEventJson));
    }

    private String stringify(RsEvent rsEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(rsEvent);
    }

    private RsEvent parse(String rsEventJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(rsEventJson, RsEvent.class);
    }
}
