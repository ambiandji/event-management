package org.crok4it.em.service.mapper;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        uses = {
                UuidMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ArtistMapper {
        @Mapping(source = "id", target = "id")
        @Mapping(source = "name", target = "name")
        @Mapping(source = "city", target = "city")
        @Mapping(source = "phone", target = "phone")
        @Mapping(source = "facebookLink", target = "facebookLink")
        @Mapping(source = "imageLink", target = "imageLink")
        @Mapping(source = "websiteLink", target = "websiteLink")
        @Mapping(source = "seekingVenue", target = "seekingVenue")
        @Mapping(source = "seekingDescription", target = "seekingDescription")
        //TODO map genres
        Artist toEntity(ArtistDTO artistDto);
}