package ru.yandex.practicum.filmorate.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum SortByType {
    YEAR("year"),
    LIKES("likes");

    public final String param;

    SortByType(String param) {
        this.param = param;
    }

    public static Optional<SortByType> valueOfParam(String param) {
        return Arrays.stream(SortByType.values())
                .filter(sortByType -> sortByType.param.equals(param))
                .findFirst();
    }
}
