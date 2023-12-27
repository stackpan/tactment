package com.ivanzkyanto.tactment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanzkyanto.tactment.entity.Address;
import com.ivanzkyanto.tactment.entity.Contact;
import com.ivanzkyanto.tactment.entity.User;
import com.ivanzkyanto.tactment.model.request.AddressCreateRequest;
import com.ivanzkyanto.tactment.model.request.AddressUpdateRequest;
import com.ivanzkyanto.tactment.model.response.AddressResponse;
import com.ivanzkyanto.tactment.model.response.ApiResponse;
import com.ivanzkyanto.tactment.repository.AddressRepository;
import com.ivanzkyanto.tactment.repository.ContactRepository;
import com.ivanzkyanto.tactment.repository.UserRepository;
import com.ivanzkyanto.tactment.security.BCrypt;
import com.ivanzkyanto.tactment.util.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Contact contact;

    @BeforeEach
    @Transactional
    void setUp() {
        addressRepository.deleteAll();
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

        contact = new Contact();
        contact.setId("contact-" + UUID.randomUUID());
        contact.setFirstName("Jonathan");
        contact.setUser(user);

        contactRepository.save(contact);
    }

    @Test
    void create() throws Exception {
        AddressCreateRequest request = AddressCreateRequest.builder()
                .country("Indonesia")
                .street("Jalan yang benar")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/contacts/" + contact.getId() + "/addresses")
                        .header("X-API-TOKEN", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated()
        ).andDo(result -> {
            ApiResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertNull(response.getData().getCity());
            assertNull(response.getData().getProvince());
            assertNull(response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void update() throws Exception {
        Address address = new Address();
        address.setId("address-" + UUID.randomUUID());
        address.setCountry("Indonesia");
        address.setStreet("Jalan yang benar");
        address.setContact(contact);
        
        addressRepository.save(address);
        
        AddressUpdateRequest request = AddressUpdateRequest.builder()
                .street("Jalan yang salah")
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/contacts/" + contact.getId() + "/addresses/" + address.getId())
                        .header("X-API-TOKEN", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk()
        ).andDo(result -> {
            ApiResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
            assertEquals(address.getId(), response.getData().getId());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertNull(response.getData().getCity());
            assertNull(response.getData().getProvince());
            assertNull(response.getData().getPostalCode());
        });
    }
}