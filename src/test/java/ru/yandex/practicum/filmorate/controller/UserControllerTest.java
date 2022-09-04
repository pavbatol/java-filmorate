package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateDateException;
import ru.yandex.practicum.filmorate.exception.ValidateEmailException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.exception.ValidateLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController ctrl;
    private User user;

    @BeforeEach
    void setUp() {
        ctrl = new UserController();
        user = new User("test@test.ru", "testLogin", LocalDate.of(1980, 7, 11));
    }

    @Test
    void should_validation_passed_when_all_field_good() throws ValidateException {
        assertEquals(0, ctrl.findAll().size(), "Size is not equal");

        assertNotNull(ctrl.add(user));

        assertEquals(1, ctrl.findAll().size(), "Size is not equal");
    }

    @Test
    void should_validation_passed_when_email_good() throws ValidateException {
        final User user2;
        user2 = new User("test@test.ru", "testLogin", LocalDate.now());

        assertEquals(0, ctrl.findAll().size(), "Size is not equal");

        assertNotNull(ctrl.add(user2));

        assertEquals(1, ctrl.findAll().size(), "Size is not equal");
    }

    @Test
    void should_validation_not_passed_when_email_not_have_dog() {
        final User user2;
        user2 = new User("testtest.ru", "testLogin", LocalDate.now());

        assertThrows(ValidateEmailException.class,
                () -> ctrl.add(user2),
                "Email validating is passed");
    }

    @Test
    void should_validation_not_passed_when_email_not_have_dot() {
        final User user2;
        user2 = new User("test@testru", "testLogin", LocalDate.now());

        assertThrows(ValidateEmailException.class,
                () -> ctrl.add(user2),
                "Email validating is passed");
    }

    @Test
    void should_validation_not_passed_when_email_have_cyrillic_before_dog() {
        final User user2;
        user2 = new User("тест@test.ru", "testLogin", LocalDate.now());

        assertThrows(ValidateEmailException.class,
                () -> ctrl.add(user2),
                "Email validating is passed");
    }

    @Test
    void should_validation_not_passed_when_email_have_cyrillic_before_dot() {
        final User user2;
        user2 = new User("test@тест.ru", "testLogin", LocalDate.now());

        assertThrows(ValidateEmailException.class,
                () -> ctrl.add(user2),
                "Email validating is passed");
    }

    @Test
    void should_validation_not_passed_when_login_is_blank() {
        final User user2;
        user2 = new User("test@тест.ru", "   ", LocalDate.now());

        assertThrows(ValidateLoginException.class,
                () -> ctrl.add(user2),
                "Login validating is passed");
    }

    @Test
    void should_validation_not_passed_when_login_have_whitespace() {
        final User user2;
        user2 = new User("test@тест.ru", "My Login", LocalDate.now());

        assertThrows(ValidateLoginException.class,
                () -> ctrl.add(user2),
                "Login validating is passed");
    }

    @Test
    void should_validation_passed_when_name_is_null() throws ValidateException {
        final User user2;
        user2 = new User("test@test.ru", "MyLogin", LocalDate.now());

        assertNull(user2.getName());

        assertEquals(0, ctrl.findAll().size(), "Size is not equal");

        assertNotNull(ctrl.add(user2));

        assertEquals(1, ctrl.findAll().size(), "Size is not equal");
    }

    @Test
    void should_validation_passed_when_name_is_blank() throws ValidateException {
        final User user2;
        user2 = new User("test@test.ru", "MyLogin", LocalDate.now());
        user2.setName("  ");

        assertEquals(0, ctrl.findAll().size(), "Size is not equal");

        assertNotNull(ctrl.add(user2));

        assertEquals(1, ctrl.findAll().size(), "Size is not equal");
    }

    @Test
    void should_name_equals_login_when_name_is_blank_or_null() throws ValidateException {
        final User user2;
        user2 = new User("test@test.ru", "MyLogin", LocalDate.now());

        user2.setName("  ");
        User addedUser = ctrl.add(user2);

        assertNotNull(addedUser);
        assertEquals(addedUser.getLogin(), addedUser.getName(), "Name not equals Login");

        user2.setName(null);
        addedUser = ctrl.add(user2);

        assertNotNull(addedUser);
        assertEquals(addedUser.getLogin(), addedUser.getName(), "Name not equals Login");
    }

    @Test
    void should_validation_not_passed_when_date_wrong() {
        final User user2;
        user2 = new User("test@test.ru", "testLogin", LocalDate.now().plusDays(1));

        assertThrows(ValidateDateException.class,
                () -> ctrl.add(user2), "Date validation passed");

    }

    @Test
    void should_validation_not_passed_when_date_good() {
        final User user2;
        user2 = new User("test@test.ru", "testLogin", LocalDate.now());

        try {
            assertNotNull(ctrl.add(user2), "Return is null");
            assertEquals(1, ctrl.findAll().size(), "Size is not equal");
        } catch (ValidateException e) {
            fail("Date validation is not passed");
        }
    }

    @Test
    void should_creating_not_passed_when_date_is_null() {

        assertThrows(NullPointerException.class,
                () -> new User("test@test.ru", "testLogin", null), "User created");

    }

}