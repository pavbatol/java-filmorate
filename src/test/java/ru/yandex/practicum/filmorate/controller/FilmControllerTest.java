package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.MockMvcTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@MockMvcTest
class FilmControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    FilmController controller;
    private Film film;
    private static final long VALID_ID = 0;
    private static final String VALID_NAME = "Test";
    private static final String VALID_DESCRIPTION = "TestTest TestTest";
    private static final LocalDate VALID_DATE = LocalDate.now().minusDays(1);
    private static final long VALID_DURATION = 1;
    private static final LocalDate CINEMA_DAY =  LocalDate.of(1895, DECEMBER, 28);
    private static final Set<Long> VALID_LIKES = null; //new HashSet<>();
    private static final LocalDate CURRENT_DAY =  LocalDate.now();

    @BeforeEach
    void setUp() {
        film = getGoodNewFilm();
    }

    @Test
    public void whenAdd_should_status_200_and_film_returned_if_all_fields_good() throws Exception {
        Mockito.when(controller.add(film)).thenReturn(film);

        mvc.perform(post("/films").
                        content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));


    }

    @Test
    public void whenAdd_should_status_400_if_name_is_blank() throws Exception {
        film = film.toBuilder().name("  ").build();

        mvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_name_is_null() {
        assertThrows(NullPointerException.class,
                () -> new Film(VALID_ID, null, VALID_DESCRIPTION, VALID_DATE, VALID_DURATION, VALID_LIKES),
                "Exception not thrown");
    }

    @Test
    public void whenAdd_should_status_400_if_description_is_longer_200() throws Exception {
        film = film.toBuilder().description(new String(new char[201])).build();

        mvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAdd_should_status_400_if_date_is_null() {
        assertThrows(NullPointerException.class,
                () -> new Film(VALID_ID, VALID_NAME, VALID_DESCRIPTION, null, VALID_DURATION, VALID_LIKES),
                "Exception not thrown");
    }

    @Test
    void whenAdd_should_status_400_if_date_is_before_cinema_day() throws Exception {
        film = new Film(VALID_ID, VALID_NAME, VALID_DESCRIPTION, CINEMA_DAY.minusDays(1), VALID_DURATION, VALID_LIKES);

        mvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAdd_should_status_400_if_date_is_future() throws Exception {
        film = new Film(VALID_ID, VALID_NAME, VALID_DESCRIPTION, CURRENT_DAY.plusDays(1), VALID_DURATION, VALID_LIKES);

        mvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_duration_is_not_positive() throws Exception {
        film = new Film(VALID_ID, VALID_NAME, VALID_DESCRIPTION, VALID_DATE, -1, VALID_LIKES);

        mvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Film getGoodNewFilm() {
        return Film.builder()
                .id(VALID_ID)
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .releaseDate(VALID_DATE)
                .duration(VALID_DURATION)
                .build();
    }
}