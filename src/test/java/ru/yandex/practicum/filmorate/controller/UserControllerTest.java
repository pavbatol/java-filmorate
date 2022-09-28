package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.MockMvcTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    UserController controller;
    private User validUser;
    private static final long VALID_ID = 0;
    private static final String VALID_EMAIL = "test@test.ru";
    private static final String VALID_LOGIN = "testLogin";
    private static final String VALID_NAME = "testName";
    private static final LocalDate VALID_DATE = LocalDate.now().minusDays(1);
    private static final Set<Long> VALID_FRIENDS = new HashSet<>();
    private static final LocalDate CURRENT_DAY =  LocalDate.now();

    @BeforeEach
    void setUp() {
        validUser = getNewValidUser();
    }

    @Test
    public void whenAdd_should_status_200_and_film_returned_if_all_fields_good() throws Exception {
        Mockito.when(controller.add(validUser)).thenReturn(validUser);

        mvc.perform(post("/users").
                        content(objectMapper.writeValueAsString(validUser)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(validUser)));
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_null() {
        assertThrows(NullPointerException.class,
                () -> validUser.toBuilder().email(null).build(),
                "Exception not thrown");
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_blank() throws Exception {
        User user = validUser.toBuilder().email("  ").build();
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_email_not_have_dog() throws Exception {
        User user = validUser.toBuilder().email("test_test.ru").build();
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_wrong() throws Exception {
        User user = validUser.toBuilder().email("@testtest.ru").build();
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_login_is_blank() throws Exception {
        User user = validUser.toBuilder().login("   ").build();
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_login_have_whitespace() throws Exception {
        User user = validUser.toBuilder().login("my login").build();
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_200_if_name_is_blank() throws Exception {
        User user = validUser.toBuilder().name("  ").build();
        Mockito.when(controller.add(user)).thenReturn(user);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void whenAdd_should_status_200_if_name_is_null() throws Exception {
        User user = validUser.toBuilder().name(null).build();
        Mockito.when(controller.add(user)).thenReturn(user);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void whenAdd_should_status_400_if_date_is_null() {
        assertThrows(NullPointerException.class,
                () -> validUser.toBuilder().birthday(null).build(),
                "Exception not thrown");
    }

    @Test
    void whenAdd_should_status_400_if_date_is_not_past() throws Exception {
        User user = validUser.toBuilder().birthday(CURRENT_DAY).build();

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private User getNewValidUser() {
//        return new User(VALID_ID, VALID_EMAIL, VALID_LOGIN, VALID_NAME, VALID_DATE, VALID_FRIENDS);
        return  User.builder()
                .id(VALID_ID)
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name(VALID_NAME)
                .birthday(VALID_DATE)
                .friends(VALID_FRIENDS)
                .build();
    }
}