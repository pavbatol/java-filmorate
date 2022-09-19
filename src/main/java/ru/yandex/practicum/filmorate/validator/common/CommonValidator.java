package ru.yandex.practicum.filmorate.validator.common;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces.Storage;

public final class CommonValidator {
    private CommonValidator() {
    }

    public static <T> T validateId(@NonNull Storage<T> storage, Long id) {
        return storage.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("id %s не найден", id)));
    }

    // TODO: 20.09.2022 Удалить закомменченное
//    public static <T> void validateId(@NonNull Storage<T> storage, Long id) {
//        if (!storage.contains(id)) {
//            throw new NotFoundException(String.format("id %s не найден", id));
//        }
//    }
//
//    public static <T> T getNonNullEntity(@NonNull Storage<T> storage, Long id) {
//        return storage.findById(id)
//                .orElseThrow(() ->
//                        new NotFoundException(String.format("id %s не найден", id)));
//    }


    public static void validatePositive(int count, String message) {
        if (count < 0) {
            throw new IllegalArgumentException(message);
        }
    }

}
