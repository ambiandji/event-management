package org.crok4it.em.unit.service.impl;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.repository.ArtistRepository;
import org.crok4it.em.service.ArtistService;
import org.crok4it.em.service.impl.ArtistServiceImpl;
import org.crok4it.em.service.mapper.ArtistMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.clearAllCaches;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DirtiesContext(classMode= BEFORE_CLASS)
public class ArtistServiceImplTest{

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

    @AfterEach
    void tearDown() {
        clearAllCaches();
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
    void createArtistWithDuplicatePhoneNumberShouldFail() throws  InvocationTargetException, IllegalAccessException {

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

    @Test
    @DisplayName("Fetch artist by name from database")
    void findArtistByExistingNameShouldSuccess() {
        String name = "name";
        Artist artist = mock(Artist.class);
        ArtistDTO artistDTO = mock(ArtistDTO.class);

        when(artistRepository.findByNameContainsIgnoreCase(name)).thenReturn(Collections.singletonList(artist));
        when(artistMapper.toDto(artist)).thenReturn(artistDTO);

        assertThat(artistService.findByName(name)).isEqualTo(Collections.singletonList(artistDTO));

        InOrder inOrder = inOrder(artistRepository, artistMapper);
        inOrder.verify(artistRepository).findByNameContainsIgnoreCase(name);
        inOrder.verify(artistMapper).toDto(artist);

    }

    @Test
    @DisplayName("Fetch all artist from database")
    void findArtisAllExistingShouldSuccess() {

        Artist artist = mock(Artist.class);
        ArtistDTO artistDTO = mock(ArtistDTO.class);

        when(artistRepository.findAll()).thenReturn(Collections.singletonList(artist));
        when(artistMapper.toDto(artist)).thenReturn(artistDTO);

        assertThat(artistService.findAll()).isEqualTo(Collections.singletonList(artistDTO));

        InOrder inOrder = inOrder(artistRepository, artistMapper);
        inOrder.verify(artistRepository).findAll();
        inOrder.verify(artistMapper).toDto(artist);

    }

    @Test
    @DisplayName("Delete artist by id from database")
    void deleteArtistByExistingIdShouldSuccess() {
        String artistId = UUID.randomUUID().toString();
        Artist artist = mock(Artist.class);

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        doNothing().when(artistRepository).deleteById(artistId);

        artistService.deleteById(artistId);

        InOrder inOrder = inOrder(artistRepository);
        inOrder.verify(artistRepository).findById(artistId);
        inOrder.verify(artistRepository).deleteById(artistId);



    }
    @Test
    @DisplayName("Delete artist with wrong id from database")
    void deleteArtistWithWrongIdShouldFail() {
        String artistId = UUID.randomUUID().toString();
        Artist artist = mock(Artist.class);

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.deleteById(artistId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Artist with id %s not found", artistId));

        InOrder inOrder = inOrder(artistRepository);
        inOrder.verify(artistRepository).findById(artistId);

    }

    @Test
    @DisplayName("Update artist by id from database")
    void updateArtistByExistingIdShouldSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String artistId = UUID.randomUUID().toString();
        Artist artist = mock(Artist.class);
        ArtistDTO artistDTO = mock(ArtistDTO.class);
        String phone = "Artist_create_phone";

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        when(artistDTO.getPhone()).thenReturn(phone);
        when(method.invoke(artistService, "findByPhone")).thenReturn(null);
        when(artistRepository.findByPhone(phone)).thenReturn(Optional.empty());

        when(artistMapper.fromDTOForUpdate(artist, artistDTO)).thenReturn(artist);
        when(artistRepository.saveAndFlush(artist)).thenReturn(artist);
        when(artistMapper.toDto(artist)).thenReturn(artistDTO);

        assertThat(artistService.update(artistId, artistDTO)).isEqualTo(artistDTO);

        InOrder inOrder = inOrder(artistRepository, artistMapper);
        inOrder.verify(artistRepository).findById(artistId);
        inOrder.verify(artistMapper).fromDTOForUpdate(artist, artistDTO);
        inOrder.verify(artistRepository).saveAndFlush(artist);



    }

    @Test
    @DisplayName("Update artist by wrong id from database")
    void updateArtistByWrongIdShouldFail() {
        String artistId = UUID.randomUUID().toString();
        ArtistDTO artistDTO = mock(ArtistDTO.class);

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.update(artistId, artistDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Artist with id %s not found", artistId));

        InOrder inOrder = inOrder(artistRepository, artistMapper);
        inOrder.verify(artistRepository).findById(artistId);



    }

}
