package com.ivanzkyanto.tactment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.tactment.entity.Contact;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.ContactCreateRequest;
import com.ivanzkyanto.tactment.model.request.ContactUpdateRequest;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.model.response.ContactResponse;
import com.ivanzkyanto.tactment.model.response.ErrorResponse;
import com.ivanzkyanto.tactment.repository.ContactRepository;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import com.ivanzkyanto.tactment.util.Token;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    private User user;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        Token token = Token.generate();

        user = new User();
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setToken(token.getToken());
        user.setTokenExpiredAt(token.getExpiredAt());

        userRepository.save(user);
    }

    @Test
    void createSuccess() throws Exception {
        ContactCreateRequest request = ContactCreateRequest.builder()
                .firstName("Juleus")
                .lastName("Caesar")
                .email("juleus.causar@example.com")
                .phone("081234567890")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk()
        ).andDo(result -> {
            ApiResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
            assertEquals(request.getFirstName(), response.getData().getFirstName());
            assertEquals(request.getLastName(), response.getData().getLastName());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertEquals(request.getPhone(), response.getData().getPhone());
        });
    }

    @Test
    void updateSuccess() throws Exception {
        Contact contact = new Contact();
        contact.setId("contact-" + UUID.randomUUID());
        contact.setUser(user);
        contact.setFirstName("Juleus");
        contact.setLastName("Caesar");
        contact.setEmail("juleus.caesar@example.com");
        contact.setPhone("081234567890");

        contactRepository.save(contact);

        ContactUpdateRequest request = ContactUpdateRequest.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email("changed." + contact.getEmail())
                .phone(contact.getPhone())
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(
                                ApiResponse.builder()
                                        .data(ContactResponse.builder()
                                                .id(contact.getId())
                                                .firstName(request.getFirstName())
                                                .lastName(request.getLastName())
                                                .email(request.getEmail())
                                                .phone(request.getPhone())
                                                .build()
                                        ).build()
                        )
                )
        );
    }

    @Test
    void getSuccess() throws Exception {
        Contact contact = new Contact();
        contact.setId("contact-" + UUID.randomUUID());
        contact.setUser(user);
        contact.setFirstName("Juleus");
        contact.setLastName("Caesar");
        contact.setEmail("juleus.caesar@example.com");
        contact.setPhone("081234567890");

        contactRepository.save(contact);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(
                                ApiResponse.builder()
                                        .data(ContactResponse.builder()
                                                .id(contact.getId())
                                                .firstName(contact.getFirstName())
                                                .lastName(contact.getLastName())
                                                .email(contact.getEmail())
                                                .phone(contact.getPhone())
                                                .build()
                                        ).build()
                        )
                )
        );
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/contacts/randomid")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(
                                ErrorResponse.builder()
                                        .errors("Contact not found")
                                        .build()
                        )
                )
        );
    }

    @Test
    void deleteSuccess() throws Exception {
        Contact contact = new Contact();
        contact.setId("contact-" + UUID.randomUUID());
        contact.setUser(user);
        contact.setFirstName("Juleus");
        contact.setLastName("Caesar");
        contact.setEmail("juleus.caesar@example.com");
        contact.setPhone("081234567890");

        contactRepository.save(contact);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("data", Matchers.equalTo("Ok"))
        );

        assertNull(contactRepository.findById(contact.getId()).orElse(null));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/contacts/fictionalId")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(
                                ErrorResponse.builder()
                                        .errors("Contact not found")
                                        .build()
                        )
                )
        );
    }
}