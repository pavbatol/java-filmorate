package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.MpaRatingDBStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.time.Month.DECEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDBStorage genreStorage;
    private final MpaRatingDBStorage mpaRatingStorage;

    //User
    private static final long VALID_ID = 0;
    private static final String VALID_EMAIL = "test@test.ru";
    private static final String VALID_LOGIN = "testLogin";
    private static final String VALID_USER_NAME = "testName";
    private static final LocalDate VALID_BIRTHDAY_DATE = LocalDate.now().minusDays(1);
    private static final Set<Long> VALID_FRIENDS = new HashSet<>();

    //Film
    private static final String VALID_FILM_NAME = "Test";
    private static final String VALID_DESCRIPTION = "TestTest TestTest";
    private static final LocalDate VALID_DATE = LocalDate.now().minusDays(1);
    private static final long VALID_DURATION = 1;
    private static final LocalDate CINEMA_DAY = LocalDate.of(1895, DECEMBER, 28);
    private static final Set<Long> VALID_LIKES = null; //new HashSet<>();
    private static final Set<Genre> VALID_GENRES = null;
    private static final MpaRating VALID_MPA = new MpaRating(1, "G", "testName"); // null

    @BeforeEach
    void setUp() {
        userStorage.clear();
        filmStorage.clear();
    }

    //User
    @Test
    void testUpdateUser() {
        User user = getNewUser();
        User added = userStorage.add(user);

        String newName = "new" + added.getName();
        String newEmail = "new" + added.getEmail();
        String newLogin = "new" + added.getLogin();
        LocalDate newBirthday = added.getBirthday().minusDays(1);

        User user2 = added.toBuilder()
                .id(added.getId())
                .name(newName)
                .email(newEmail)
                .login(newLogin)
                .birthday(newBirthday)
                .build();

        User updated = userStorage.update(user2);

        Optional<User> userOptional = userStorage.findById(added.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(us -> {
                            assertThat(us).hasFieldOrPropertyWithValue("id", added.getId());
                            assertThat(us).hasFieldOrPropertyWithValue("email", newEmail);
                            assertThat(us).hasFieldOrPropertyWithValue("name", newName);
                            assertThat(us).hasFieldOrPropertyWithValue("login", newLogin);
                            assertThat(us).hasFieldOrPropertyWithValue("birthday", newBirthday);
                        }
                );
    }

    @Test
    public void testFindUserWithBaseIsEmpty() {
        Optional<User> userOptional = userStorage.findById(1L);

        assertThat(userOptional).isEmpty();
    }

    @Test
    public void testFindUserByBadId() {
        User user = getNewUser();
        userStorage.add(user);
        Optional<User> userOptional = userStorage.findById(900L);

        assertThat(userOptional).isEmpty();
    }

    @Test
    public void testFindUserByValidId() {
        User user = getNewUser();
        User added1 = userStorage.add(user);
        User added2 = userStorage.add(user);
        Optional<User> userOptional = userStorage.findById(added1.getId());

        assertThat(userStorage.findAll()).hasSize(2);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", added1.getId())
                );
    }

    @Test
    void testFindAllValidUsers() {
        User user = getNewUser();
        userStorage.add(user);
        userStorage.add(user);

        assertThat(userStorage.findAll()).hasSize(2);
    }

    @Test
    void testFindUserFriends() {
        User user = getNewUser();
        User added1 = userStorage.add(user);
        User added2 = userStorage.add(user);
        User added3 = userStorage.add(user);
        userStorage.addFriend(added1.getId(), added2.getId());
        userStorage.addFriend(added1.getId(), added3.getId());
        List<User> users = userStorage.findAll();
        List<User> friends = userStorage.findFriends(added1.getId());

        assertThat(users).hasSize(3);
        assertThat(friends).hasSize(2);
        assertThat(friends.get(0)).hasFieldOrPropertyWithValue("id", added2.getId());
        assertThat(friends.get(1)).hasFieldOrPropertyWithValue("id", added3.getId());
    }

    @Test
    void testFindMutualFriends() {
        User user = getNewUser();
        User added1 = userStorage.add(user);
        User added2 = userStorage.add(user);
        User added3 = userStorage.add(user);
        userStorage.addFriend(added1.getId(), added2.getId());
        userStorage.addFriend(added1.getId(), added3.getId());
        userStorage.addFriend(added2.getId(), added3.getId());

        List<User> mutualFriends = userStorage.findMutualFriends(added1.getId(), added2.getId());
        assertThat(mutualFriends).hasSize(1);
        assertThat(mutualFriends.get(0)).hasFieldOrPropertyWithValue("id", added3.getId());
    }

    //Film
    @Test
    void testUpdateFilm() {
        Film film = getNewValidFilm();
        Film added = filmStorage.add(film);

        String newName = "new" + added.getName();
        String newDescription = "new" + added.getDescription();
        LocalDate newReleaseDate = added.getReleaseDate().minusDays(1);
        long newDuration = added.getDuration() + 1;
        int newRate = added.getRate() + 1;

        Film film2 = added.toBuilder()
                .id(added.getId())
                .name(newName)
                .description(newDescription)
                .releaseDate(newReleaseDate)
                .duration(newDuration)
                .rate(newRate)
                .build();

        Film updated = filmStorage.update(film2);

        Optional<Film> userOptional = filmStorage.findById(added.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(fm -> {
                            assertThat(fm).hasFieldOrPropertyWithValue("id", added.getId());
                            assertThat(fm).hasFieldOrPropertyWithValue("name", newName);
                            assertThat(fm).hasFieldOrPropertyWithValue("description", newDescription);
                            assertThat(fm).hasFieldOrPropertyWithValue("releaseDate", newReleaseDate);
                            assertThat(fm).hasFieldOrPropertyWithValue("duration", newDuration);
                            assertThat(fm).hasFieldOrPropertyWithValue("rate", newRate);
                        }
                );
    }

    @Test
    void testRemoveFilm() {
        Film film = getNewValidFilm();
        Film added1 = filmStorage.add(film);
        Film added2 = filmStorage.add(film);
        List<Film> all = filmStorage.findAll();

        assertThat(all).hasSize(2);

        filmStorage.remove(added2.getId());
        all = filmStorage.findAll();

        assertThat(all).hasSize(1);
    }

    @Test
    void testFindFilmById() {
        Film film = getNewValidFilm();
        Film added1 = filmStorage.add(film);
        Film added2 = filmStorage.add(film);

        Optional<Film> byId = filmStorage.findById(added2.getId());

        assertThat(byId)
                .isPresent()
                .hasValueSatisfying(fm -> {
                            assertThat(fm).hasFieldOrPropertyWithValue("id", added2.getId());
                        }
                );

    }

    @Test
    void testFindPopularFilms() {
        User user = getNewUser();
        User uAdded1 = userStorage.add(user);
        User uAdded2 = userStorage.add(user);

        Film film = getNewValidFilm();
        Film added1 = filmStorage.add(film);
        Film added2 = filmStorage.add(film);
        Film added3 = filmStorage.add(film);

        filmStorage.addLike(added2.getId(), uAdded1.getId());
        filmStorage.addLike(added3.getId(), uAdded1.getId());
        filmStorage.addLike(added3.getId(), uAdded2.getId());

        List<Film> allFilms = filmStorage.findAll();

        assertThat(allFilms).hasSize(3);

        List<Film> popularFilms = filmStorage.findPopularFilms(1);

        assertThat(popularFilms).hasSize(1);
        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("id", added3.getId());

        filmStorage.removeLike(added2.getId(), uAdded1.getId());
        popularFilms = filmStorage.findPopularFilms(1);
        assertThat(popularFilms).hasSize(1);
        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("id", added3.getId());
    }

    //Genre
    @Test
    void testUpdateGenre() {
        Genre genre2 = getNewGenre();
        String newName = "new" + genre2.getName();
        genre2.setId(1);
        genre2.setName(newName);

        genreStorage.update(genre2);

        Optional<Genre> byId = genreStorage.findById(1L);

        assertThat(byId)
                .isPresent()
                .hasValueSatisfying(us -> {
                            assertThat(us).hasFieldOrPropertyWithValue("name", newName);
                        }
                );
    }

    @Test
    void testFindAllGenres() {
        List<Genre> all = genreStorage.findAll();
        assertThat(all).hasSize(6);
    }

    //MpaRating
    @Test
    void testUpdateMpaRating() {
        MpaRating mpaRating2 = getNewMpaRating();
        String newName = "_";
        String description = "new" + mpaRating2.getDescription();
        mpaRating2.setId(1);
        mpaRating2.setName(newName);
        mpaRating2.setDescription(description);

        mpaRatingStorage.update(mpaRating2);

        Optional<MpaRating> byId = mpaRatingStorage.findById(1L);

        assertThat(byId)
                .isPresent()
                .hasValueSatisfying(us -> {
                            assertThat(us).hasFieldOrPropertyWithValue("name", newName);
                            assertThat(us).hasFieldOrPropertyWithValue("description", description);
                        }
                );
    }

    @Test
    void testFindAllMpaRatings() {
        List<MpaRating> all = mpaRatingStorage.findAll();
        assertThat(all).hasSize(5);
    }

    //**
    private User getNewUser() {
        return User.builder()
                .id(VALID_ID)
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name(VALID_USER_NAME)
                .birthday(VALID_DATE)
                .friends(VALID_FRIENDS)
                .build();
    }

    private Film getNewValidFilm() {
        return Film.builder()
                .id(VALID_ID)
                .name(VALID_FILM_NAME)
                .description(VALID_DESCRIPTION)
                .releaseDate(VALID_DATE)
                .duration(VALID_DURATION)
                .likes(VALID_LIKES)
                .genres(VALID_GENRES)
                .mpa(VALID_MPA)
                .rate(0)
                .build();
    }

    private Genre getNewGenre() {
        return new Genre(0, "testName");
    }

    private MpaRating getNewMpaRating() {
        return new MpaRating(1, "testName", "testDescription");
    }
}
