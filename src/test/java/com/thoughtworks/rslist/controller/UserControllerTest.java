package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

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
    void should_get_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name").exists());
    }

    @Test
    void should_get_error_when_use_base_controller() throws Exception {
        mockMvc.perform(get("/users/error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_insert_user() throws Exception {
        User user = createFakeUser();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toJson()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is("/users/1")));

        List<UserEntity> all = userRepository.findAll();

        assertEquals(all.size(), 1);

        UserEntity insertedUserEntity = all.get(0);
        assertEquals(insertedUserEntity.getUserName(), user.getUserName());
        assertEquals(insertedUserEntity.getAge(), user.getAge());
        assertEquals(insertedUserEntity.getEmail(), user.getEmail());
        assertEquals(insertedUserEntity.getGender(), user.getGender());
        assertEquals(insertedUserEntity.getPhone(), user.getPhone());
    }

    @Test
    void should_get_user_by_id() throws Exception {
        UserEntity fakeUserEntity = createFakeUserEntity();

        fakeUserEntity = userRepository.save(fakeUserEntity);

        assertEquals(fakeUserEntity.getId(), 1);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id", is(fakeUserEntity.getId())))
                .andExpect(jsonPath("$.user_name", is(fakeUserEntity.getUserName())))
                .andExpect(jsonPath("$.user_age", is(fakeUserEntity.getAge())))
                .andExpect(jsonPath("$.user_gender", is(fakeUserEntity.getGender())))
                .andExpect(jsonPath("$.user_email", is(fakeUserEntity.getEmail())))
                .andExpect(jsonPath("$.user_phone", is(fakeUserEntity.getPhone())));
    }

    @Test
    void should_delete_user_by_id() throws Exception {
        UserEntity fakeUserEntity = createFakeUserEntity();

        fakeUserEntity = userRepository.save(fakeUserEntity);

        mockMvc.perform(delete("/users/" + fakeUserEntity.getId()))
                .andExpect(status().isOk());

        assertFalse(userRepository.existsById(fakeUserEntity.getId()));
    }

    @Test
    void should_delete_user_and_all_events() throws Exception {
        UserEntity fakeUserEntity = userRepository.save(createFakeUserEntity());
        Integer userId = fakeUserEntity.getId();

        rsEventRepository.save(RsEventEntity.builder()
                .eventName("1")
                .keyword("1")
                .user(fakeUserEntity)
                .build());

        rsEventRepository.save(RsEventEntity.builder()
                .eventName("2")
                .keyword("2")
                .user(fakeUserEntity)
                .build());

        mockMvc.perform(delete("/users/" + fakeUserEntity.getId()))
                .andExpect(status().isOk());

        assertFalse(userRepository.existsById(fakeUserEntity.getId()));
        assertTrue(rsEventRepository.findAllByUserId(userId).isEmpty());
    }

    private UserEntity createFakeUserEntity() {
        return UserEntity.builder()
                .userName("name")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("10000000000")
                .build();
    }

    private User createFakeUser() {
        return User.builder()
                .userName("name")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("10000000000")
                .build();
    }
}
