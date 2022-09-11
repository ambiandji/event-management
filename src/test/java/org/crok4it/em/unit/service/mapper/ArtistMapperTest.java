package org.crok4it.em.unit.service.mapper;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.service.mapper.ArtistMapper;
import org.crok4it.em.service.mapper.ArtistMapperImpl;
import org.crok4it.em.service.mapper.UuidMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ArtistMapperTest {

    private ArtistMapper artistMapper;
    private ArtistDTO artistDTO;
    private Artist artist;
    private UUID id = UUID.randomUUID();
    private String name = "Artist_name";
    private String city = "Artist_city";
    private String phone = "Artist_phone";
    private String imageLink = "https://Artist_imageLink.com";;
    private String facebookLink = "https://Artist_facebookLink.com";;
    private String websiteLink = "https://Artist_websiteLink.com";
    private Boolean seekingVenue = true;
    private String seekingDescription = "Artist_seekingDescription";

    @BeforeEach
    void setup() {
        artistDTO = new ArtistDTO()
                .id(id)
                .name(name)
                .phone(phone)
                .city(city)
                .imageLink(imageLink)
                .facebookLink(facebookLink)
                .websiteLink(websiteLink)
                .seekingVenue(seekingVenue)
                .seekingDescription(seekingDescription);

        artist= Artist.builder()
                .id(id.toString())
                .name(name)
                .phone(phone)
                .city(city)
                .imageLink(imageLink)
                .facebookLink(facebookLink)
                .websiteLink(websiteLink)
                .seekingVenue(seekingVenue)
                .seekingDescription(seekingDescription).build();

        artistMapper = new ArtistMapperImpl(new UuidMapperImpl());
    }

    @Test
    @DisplayName("Convert artist dto to entity")
    void dtoToEntityTest() {
       Artist artist = artistMapper.toEntity(artistDTO);

        assertThat(artist)
                .isNotNull()
                .returns(id.toString(), Artist::getId)
                .returns(name, Artist::getName)
                .returns(phone, Artist::getPhone)
                .returns(city, Artist::getCity)
                .returns(facebookLink, Artist::getFacebookLink)
                .returns(imageLink, Artist::getImageLink)
                .returns(websiteLink, Artist::getWebsiteLink)
                .returns(seekingVenue, Artist::getSeekingVenue)
                .returns(seekingDescription, Artist::getSeekingDescription);
    }

    @Test
    @DisplayName("Convert artist entity to dto")
    void entityToDtoTest() {
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        assertThat(artistDTO)
                .isNotNull()
                .returns(id, ArtistDTO::getId)
                .returns(name, ArtistDTO::getName)
                .returns(phone, ArtistDTO::getPhone)
                .returns(city, ArtistDTO::getCity)
                .returns(facebookLink, ArtistDTO::getFacebookLink)
                .returns(imageLink, ArtistDTO::getImageLink)
                .returns(websiteLink, ArtistDTO::getWebsiteLink)
                .returns(seekingVenue, ArtistDTO::getSeekingVenue)
                .returns(seekingDescription, ArtistDTO::getSeekingDescription);
    }
}
