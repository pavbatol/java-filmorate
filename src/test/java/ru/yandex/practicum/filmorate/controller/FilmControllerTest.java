package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.MockMvcTest;

import java.time.LocalDate;
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
    private Film validFilm;
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
        validFilm = getNewValidFilm();
    }

    @Test
    public void whenAdd_should_status_200_and_film_returned_if_all_fields_good() throws Exception {
        Mockito.when(controller.add(validFilm)).thenReturn(validFilm);

        mvc.perform(post("/films").
                        content(objectMapper.writeValueAsString(validFilm)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(validFilm)));


    }

    @Test
    public void whenAdd_should_status_400_if_name_is_blank() throws Exception {
        Film film = validFilm.toBuilder().name("  ").build();

        mvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_name_is_null() {
        assertThrows(NullPointerException.class,
                () -> validFilm.toBuilder().name(null).build(),
                "Exception not thrown");
    }

    @Test
    public void whenAdd_should_status_400_if_description_is_longer_200() throws Exception {
        Film film = validFilm.toBuilder().description(new String(new char[201])).build();

        mvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAdd_should_status_400_if_date_is_null() {
        assertThrows(NullPointerException.class,
                () -> validFilm.toBuilder().releaseDate(null).build(),
                "Exception not thrown");
    }

    @Test
    void whenAdd_should_status_400_if_date_is_before_cinema_day() throws Exception {
        Film film = validFilm.toBuilder().releaseDate(CINEMA_DAY.minusDays(1)).build();

        mvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAdd_should_status_400_if_date_is_future() throws Exception {
        Film film = validFilm.toBuilder().releaseDate(CURRENT_DAY.plusDays(1)).build();

        mvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdd_should_status_400_if_duration_is_not_positive() throws Exception {
        Film film = validFilm.toBuilder().duration(-1).build();

        mvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Film getNewValidFilm() {
        return Film.builder()
                .id(VALID_ID)
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .releaseDate(VALID_DATE)
                .duration(VALID_DURATION)
                .likes(VALID_LIKES)
                .build();
    }
}