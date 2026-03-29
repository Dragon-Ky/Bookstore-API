package com.example.bookstore.Mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapperHelper {

    @Named("stringToSet")
    public Set<String> stringToSet(String categories) {
        if (categories == null || categories.isBlank()) {
            return Set.of();
        }

        return Arrays.stream(categories.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}