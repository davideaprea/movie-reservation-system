package com.example.demo.integration;

import com.example.demo.config.DBManager;
import com.example.demo.config.TestcontainersConfig;
import com.example.demo.security.entity.User;
import com.example.demo.core.enumeration.Routes;
import com.example.demo.security.dto.LoginDto;
import com.example.demo.security.dto.RegisterDto;
import com.example.demo.security.repository.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@TestExecutionListeners(value = DBManager.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDao userDao;
    private final ObjectMapper objMapper = new ObjectMapper();

    @Test
    void shouldRegisterUser() throws Exception {
        final String email = "user@gmail.com";
        RegisterDto dto = new RegisterDto(email, "psw");

        registerUser(dto).andExpect(status().isCreated());

        Optional<User> newUser = userDao.findByEmail(email);

        assertTrue(newUser.isPresent());
    }

    @Test
    void shouldAvoidEmailDuplicates() throws Exception {
        RegisterDto dto = new RegisterDto("user@gmail.com", "psw");

        registerUser(dto);
        registerUser(dto).andExpect(status().isConflict());

        assertEquals(1, userDao.count());
    }

    @Test
    void shouldReturnBadRequestError() throws Exception {
        RegisterDto dto = new RegisterDto("user.com", "");

        registerUser(dto).andExpect(status().isBadRequest());

        assertEquals(0, userDao.count());
    }

    @Test
    void shouldLoginAUser() throws Exception {
        final String email = "user@gmail.com";
        final String psw = "password123";

        registerUser(new RegisterDto(email, psw));

        logInUser(new LoginDto(email, psw))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().string("Authorization", Matchers.startsWith("Bearer ")));
    }

    @Test
    void shouldNotLoginAUser() throws Exception {
        registerUser(new RegisterDto("user@gmail.com", "password123"));

        logInUser(new LoginDto("wrong@gmail.com", "wrong123"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("Authorization"));
    }

    private ResultActions registerUser(RegisterDto dto) throws Exception {
        String payload = objMapper.writeValueAsString(dto);

        return mockMvc.perform(MockMvcRequestBuilders
                        .post(Routes.AUTH + Routes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload));
    }

    private ResultActions logInUser(LoginDto dto) throws Exception {
        String payload = objMapper.writeValueAsString(dto);

        return mockMvc.perform(MockMvcRequestBuilders
                        .post(Routes.AUTH + Routes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload));
    }
}
