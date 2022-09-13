package org.crok4it.em.service.mapper;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {
                UuidMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ArtistMapper {

        Artist toEntity(ArtistDTO artistDto);


        ArtistDTO toDto(Artist artist);

        @Mapping(target = "id", ignore = true)
        Artist fromDTOForUpdate(@MappingTarget Artist artist, ArtistDTO artistDTO);

}
