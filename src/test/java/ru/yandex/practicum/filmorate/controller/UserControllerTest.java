package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
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
    private User user;
    private static final long VALID_ID = 0;
    private static final String VALID_EMAIL = "test@test.ru";
    private static final String VALID_LOGIN = "testLogin";
    private static final String VALID_NAME = "testName";
    private static final LocalDate VALID_DATE = LocalDate.now().minusDays(1);
    private static final Set<Long> VALID_FRIENDS = new HashSet<>();
    private static final LocalDate CURRENT_DAY =  LocalDate.now();

    @BeforeEach
    void setUp() {
        user = getNewValidUser();
    }

    @Test
    public void whenAdd_should_status_200_and_film_returned_if_all_fields_good() throws Exception {
        Mockito.when(controller.add(user)).thenReturn(user);

        mvc.perform(post("/users").
                        content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_null() {
        assertThrows(NullPointerException.class,
                () -> new User(VALID_ID, null, VALID_LOGIN, VALID_NAME, VALID_DATE, VALID_FRIENDS),
                "Exception not thrown");
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_blank() throws Exception {
        user = new User(VALID_ID, "  ", VALID_LOGIN, VALID_NAME, VALID_DATE, VALID_FRIENDS);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_email_not_have_dog() throws Exception {
        user = new User(VALID_ID, "test_test.ru", VALID_LOGIN, VALID_NAME, VALID_DATE, VALID_FRIENDS);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_wrong() throws Exception {
        user = new User(VALID_ID, "@testtest.ru", VALID_LOGIN, VALID_NAME, VALID_DATE, VALID_FRIENDS);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_login_is_blank() throws Exception {
        user = new User(VALID_ID, VALID_EMAIL, "   ", VALID_NAME, VALID_DATE, VALID_FRIENDS);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_login_have_whitespace() throws Exception {
        user = new User(VALID_ID, VALID_EMAIL, "my login", VALID_NAME, VALID_DATE, VALID_FRIENDS);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_200_if_name_is_blank() throws Exception {
        user.setName("  ");
        Mockito.when(controller.add(user)).thenReturn(user);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void whenAdd_should_status_200_if_name_is_null() throws Exception {
        user.setName(null);
        Mockito.when(controller.add(user)).thenReturn(user);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void whenAdd_should_status_400_if_date_is_null() {
        assertThrows(NullPointerException.class,
                () -> new User(VALID_ID, VALID_EMAIL, VALID_LOGIN, VALID_NAME, null, VALID_FRIENDS),
                "Exception not thrown");
    }

    @Test
    void whenAdd_should_status_400_if_date_is_not_past() throws Exception {
        user = new User(VALID_ID, VALID_EMAIL, VALID_LOGIN, VALID_NAME, CURRENT_DAY, VALID_FRIENDS);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private User getNewValidUser() {
        return new User(VALID_ID, VALID_EMAIL, VALID_LOGIN, VALID_NAME, VALID_DATE, VALID_FRIENDS);
    }
}