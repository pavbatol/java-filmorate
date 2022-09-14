package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    UserController controller;
    private User user;
    private static final long GOOD_ID = 0;
    private static final String GOOD_EMAIL = "test@test.ru";
    private static final String GOOD_LOGIN = "testLogin";
    private static final String GOOD_NAME = "testName";
    private static final LocalDate GOOD_DATE = LocalDate.now().minusDays(1);
    private static final LocalDate CURRENT_DAY =  LocalDate.now();

    @BeforeEach
    void setUp() {
        controller.clearStorage();
        user = getGoodNewUser();
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
                () -> new User(GOOD_ID, null, GOOD_LOGIN, GOOD_NAME, GOOD_DATE),
                "Exception not thrown");
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_blank() throws Exception {
        user = new User(GOOD_ID, "  ", GOOD_LOGIN, GOOD_NAME, GOOD_DATE);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_email_not_have_dog() throws Exception {
        user = new User(GOOD_ID, "test_test.ru", GOOD_LOGIN, GOOD_NAME, GOOD_DATE);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_email_is_wrong() throws Exception {
        user = new User(GOOD_ID, "@testtest.ru", GOOD_LOGIN, GOOD_NAME, GOOD_DATE);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_login_is_blank() throws Exception {
        user = new User(GOOD_ID, GOOD_EMAIL, "   ", GOOD_NAME,  GOOD_DATE);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_login_have_whitespace() throws Exception {
        user = new User(GOOD_ID, GOOD_EMAIL, "my login", GOOD_NAME, GOOD_DATE);

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
                () -> new User(GOOD_ID, GOOD_EMAIL, GOOD_LOGIN, GOOD_NAME, null),
                "Exception not thrown");
    }

    @Test
    void whenAdd_should_status_400_if_date_is_not_past() throws Exception {
        user = new User(GOOD_ID, GOOD_EMAIL, GOOD_LOGIN, GOOD_NAME, CURRENT_DAY);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private User getGoodNewUser() {
        return new User(GOOD_ID, GOOD_EMAIL, GOOD_LOGIN, GOOD_NAME, GOOD_DATE);
    }
}