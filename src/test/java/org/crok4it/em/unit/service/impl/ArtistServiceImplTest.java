package org.crok4it.em.unit.service.impl;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.repository.ArtistRepository;
import org.crok4it.em.service.ArtistService;
import org.crok4it.em.service.impl.ArtistServiceImpl;
import org.crok4it.em.service.mapper.ArtistMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;

public class ArtistServiceImplTest {

    ArtistMapper artistMapper;
    ArtistRepository artistRepository;
    ArtistService artistService;

    Method method;

    @BeforeEach
    void setUp() {
        artistMapper = mock(ArtistMapper.class);
        artistRepository = mock(ArtistRepository.class);
        artistService = new ArtistServiceImpl(artistMapper, artistRepository);
        method = mock(Method.class);
    }

    @Test
    @DisplayName("Create artist with valid data")
    void createArtistShouldSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        UUID artistId = UUID.randomUUID();
        ArtistDTO artistDto = mock(ArtistDTO.class);
        Artist artist = mock(Artist.class);
        String phone = "Artist_create_phone";

        when(artistMapper.toEntity(artistDto)).thenReturn(artist);

        when(artistDto.getPhone()).thenReturn(phone);
        when(method.invoke(artistService, "findByPhone")).thenReturn(null);
        when(artistRepository.findByPhone(phone)).thenReturn(Optional.empty());

        when(artistRepository.saveAndFlush(artist)).thenReturn(artist);
        when(artist.getId()).thenReturn(artistId.toString());

        assertThat(artistService.createArtist(artistDto)).isEqualTo(artistId);

        InOrder inOrder = inOrder(artistMapper, artistRepository);
        inOrder.verify(artistRepository).findByPhone(phone);
        inOrder.verify(artistMapper).toEntity(artistDto);
        inOrder.verify(artistRepository).saveAndFlush(artist);

    }

    @Test
    @DisplayName("Create artist with duplicate phone number will fail")
    void createArtistWithDuplicatePhoneNumberShouldFail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        UUID artistId = UUID.randomUUID();
        ArtistDTO artistDto = mock(ArtistDTO.class);
        Artist artist = mock(Artist.class);
        String phone = "Artist_create_phone";

        when(artistMapper.toEntity(artistDto)).thenReturn(artist);

        when(artistDto.getPhone()).thenReturn(phone);
        when(method.invoke(artistService, "findByPhone")).thenReturn(artist);
        when(artistRepository.findByPhone(phone)).thenReturn(Optional.of(artist));

        when(artistRepository.saveAndFlush(artist)).thenThrow(ConflictException.class);

        assertThatThrownBy(() -> artistService.createArtist(artistDto))
                .isInstanceOf(ConflictException.class)
                .hasMessage(String.format("Phone number %s already used. Please change", phone));

        InOrder inOrder = inOrder(artistRepository);
        inOrder.verify(artistRepository).findByPhone(phone);

    }

    @Test
    @DisplayName("Fetch artist by id from database")
    void findArtistByExistingIdShouldSuccess() {
        String artistId = UUID.randomUUID().toString();
        Artist artist = mock(Artist.class);
        ArtistDTO artistDTO = mock(ArtistDTO.class);

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        when(artistMapper.toDto(artist)).thenReturn(artistDTO);

        assertThat(artistService.findById(artistId)).isEqualTo(artistDTO);

        InOrder inOrder = inOrder(artistRepository, artistMapper);
        inOrder.verify(artistRepository).findById(artistId);
        inOrder.verify(artistMapper).toDto(artist);

    }
    @Test
    @DisplayName("Fetch artist with wrong id from database")
    void findArtistWithWrongIdShouldFail() {
        String artistId = UUID.randomUUID().toString();

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.findById(artistId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Artist with id %s not found", artistId));

        InOrder inOrder = inOrder(artistRepository);
        inOrder.verify(artistRepository).findById(artistId);


    }

}
