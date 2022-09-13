package org.crok4it.em.unit.service.mapper;

import org.crok4it.em.domain.Venue;
import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.service.mapper.UuidMapperImpl;
import org.crok4it.em.service.mapper.VenueMapper;
import org.crok4it.em.service.mapper.VenueMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class VenueMapperTest {

    private VenueMapper venueMapper;
    private VenueDTO venueDTO;
    private Venue venue;
    private UUID id = UUID.randomUUID();
    private String name = "Venue_name";
    private String city = "Venue_city";
    private String phone = "Venue_phone";
    private String imageLink = "https://Venue_imageLink.com";;
    private String facebookLink = "https://Venue_facebookLink.com";;
    private String websiteLink = "https://Venue_websiteLink.com";
    private Boolean seekingTalent = true;
    private String seekingDescription = "Venue_seekingDescription";

    @BeforeEach
    void setup() {
        venueDTO = new VenueDTO()
                .id(id)
                .name(name)
                .phone(phone)
                .city(city)
                .imageLink(imageLink)
                .facebookLink(facebookLink)
                .websiteLink(websiteLink)
                .seekingTalent(seekingTalent)
                .seekingDescription(seekingDescription);

        venue= Venue.builder()
                .id(id.toString())
                .name(name)
                .phone(phone)
                .city(city)
                .imageLink(imageLink)
                .facebookLink(facebookLink)
                .websiteLink(websiteLink)
                .seekingTalent(seekingTalent)
                .seekingDescription(seekingDescription).build();

        venueMapper = new VenueMapperImpl(new UuidMapperImpl());
    }

    @Test
    @DisplayName("Convert venue dto to entity")
    void dtoToEntityTest() {
       Venue venue = venueMapper.toEntity(venueDTO);

        assertThat(venue)
                .isNotNull()
                .returns(id.toString(), Venue::getId)
                .returns(name, Venue::getName)
                .returns(phone, Venue::getPhone)
                .returns(city, Venue::getCity)
                .returns(facebookLink, Venue::getFacebookLink)
                .returns(imageLink, Venue::getImageLink)
                .returns(websiteLink, Venue::getWebsiteLink)
                .returns(seekingTalent, Venue::getSeekingTalent)
                .returns(seekingDescription, Venue::getSeekingDescription);
    }

    @Test
    @DisplayName("Convert venue entity to dto")
    void entityToDtoTest() {
        VenueDTO venueDTO = venueMapper.toDto(venue);

        assertThat(venueDTO)
                .isNotNull()
                .returns(id, VenueDTO::getId)
                .returns(name, VenueDTO::getName)
                .returns(phone, VenueDTO::getPhone)
                .returns(city, VenueDTO::getCity)
                .returns(facebookLink, VenueDTO::getFacebookLink)
                .returns(imageLink, VenueDTO::getImageLink)
                .returns(websiteLink, VenueDTO::getWebsiteLink)
                .returns(seekingTalent, VenueDTO::getSeekingTalent)
                .returns(seekingDescription, VenueDTO::getSeekingDescription);
    }

    @Test
    @DisplayName("Convert dto to entity fetch from database")
    void fromDTOtoEntityForUpdate() {
        venueDTO.setId(null);

        venue.setPhone(null);
        venue.setName(null);
        venue.setCity(null);

        venue = venueMapper.fromDTOForUpdate(venue, venueDTO);
         assertThat(venue)
                 .isNotNull()
                 .returns(venueDTO.getName(), Venue::getName)
                 .returns(venueDTO.getPhone(), Venue::getPhone)
                 .returns(venueDTO.getCity(), Venue::getCity);
    }
}
