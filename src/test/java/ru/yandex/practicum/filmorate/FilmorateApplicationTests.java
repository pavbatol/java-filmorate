package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.MpaRatingDBStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmDbStorage;
	private final GenreDBStorage genreDBStorage;
	private final MpaRatingDBStorage mpaRatingDBStorage;

	@Test
	public void testFindUserById() {
		Optional<User> userOptional = userStorage.findById(900L);
		assertThat(userOptional).isEmpty();

//		assertThat(userOptional)
//				.isPresent()
//				.hasValueSatisfying(user ->
//						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//				);

//		String sad
	}

}
