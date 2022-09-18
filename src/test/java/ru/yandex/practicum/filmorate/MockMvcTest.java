package ru.yandex.practicum.filmorate;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@SpringBootTest
@TestPropertySource(properties = "") /* в дальнейшем попробую использовать свойства или через файл*/
@AutoConfigureMockMvc(addFilters=false)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockMvcTest {
}
