package com.ivanzkyanto.tactment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.UserLoginRequest;
import com.ivanzkyanto.tactment.model.request.UserRegisterRequest;
import com.ivanzkyanto.tactment.model.request.UserUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.UserLoginResponse;
import com.ivanzkyanto.tactment.model.response.UserResponse;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
        UserRegisterRequest request = UserRegisterRequest.builder()
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
        UserRegisterRequest request = UserRegisterRequest.builder()
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

        UserRegisterRequest request = UserRegisterRequest.builder()
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

    @Test
    void getCurrentSuccess() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk()
        ).andDo(result -> {
            ApiResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertEquals("johndoe", response.getData().getUsername());
            Assertions.assertEquals("John Doe", response.getData().getName());
        });
    }

    @Test
    void getCurrentWithoutToken() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content().string(Matchers.containsString("Unauthorized"))
        );
    }

    @Test
    void getCurrentUserNotFound() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        user.setToken(null);
        userRepository.save(user);

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isUnauthorized(),
                MockMvcResultMatchers.content().string(Matchers.containsString("Unauthorized"))
        );
    }

    @Test
    void updateSuccess() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("John Doe");

        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("johndoe")
                .password("password")
                .build();

        MvcResult loginMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andReturn();

        ApiResponse<UserLoginResponse> loginResponse = objectMapper.readValue(loginMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name("Doe John")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", loginResponse.getData().getToken())
                        .content(objectMapper.writeValueAsString(updateRequest))
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk()
        ).andDo(result -> {
            ApiResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertEquals("johndoe", response.getData().getUsername());
            Assertions.assertEquals("Doe John", response.getData().getName());
        });
    }
}
