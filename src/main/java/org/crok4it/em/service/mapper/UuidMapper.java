package org.crok4it.em.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Optional;
import java.util.UUID;

@Mapper(
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UuidMapper {
    default String map(UUID value) {
        return Optional.ofNullable(value)
                .map(UUID::toString)
                .orElse(null);
    }

    default UUID map(String value) {
        return Optional.ofNullable(value)
                .map(UUID::fromString)
                .orElse(null);
    }
}
