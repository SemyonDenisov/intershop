package ru.practicum.yandex.paging;

public record Paging(
        int pageNumber,
        int pageSize,
        boolean hasNext,
        boolean hasPrevious
) {}
