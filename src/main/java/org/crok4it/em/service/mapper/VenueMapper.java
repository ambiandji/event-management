package org.crok4it.em.service.mapper;

import org.crok4it.em.domain.Venue;
import org.crok4it.em.dto.VenueDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {UuidMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface VenueMapper {
    Venue toEntity(VenueDTO artistDto);


    VenueDTO toDto(Venue artist);

    @Mapping(target = "id", ignore = true)
    Venue fromDTOForUpdate(@MappingTarget Venue venue, VenueDTO venueDTO);
}
