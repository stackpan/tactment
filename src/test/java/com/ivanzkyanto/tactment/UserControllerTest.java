package com.ivanzkyanto.tactment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.RegisterUserRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerSuccess() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("johndoe")
                .password("password")
                .name("John Doe")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(
                                ApiResponse.builder().data("Ok").build()
                        )
                )
        );
    }

    @Test
    void registerBadRequest() throws Exception {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("johndoe")
                .password("")
                .name("John Doe")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.content().string(Matchers.containsString("password"))
        );
    }

    @Test
    void registerAlreadyRegistered() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("johndoe")
                .password("password")
                .name("John Doe")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.content().string(Matchers.containsString("Username is already registered"))
        );
    }
}
