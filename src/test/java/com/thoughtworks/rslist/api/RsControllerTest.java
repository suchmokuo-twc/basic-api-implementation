package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    void should_update_rs_event() throws Exception {
        String rsEventWithNewKeywordJson = new RsEvent(null, "分类1").toJson();

        mockMvc.perform(put("/rs/events/1")
                .content(rsEventWithNewKeywordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyword", is("分类1")))
                .andExpect(jsonPath("$.eventName", is("第一条事件")));

        String rsEventWithNewNameJson = new RsEvent("new name", null).toJson();

        mockMvc.perform(put("/rs/events/1")
                .content(rsEventWithNewNameJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyword", is("分类1")))
                .andExpect(jsonPath("$.eventName", is("new name")));
    }

    @Test
    void should_delete_rs_event() throws Exception {
        String deletedRsEventJson = new RsEvent("第一条事件", "无分类").toJson();

        mockMvc.perform(delete("/rs/event/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(deletedRsEventJson));
    }
}
