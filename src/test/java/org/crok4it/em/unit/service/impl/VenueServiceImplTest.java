package org.crok4it.em.unit.service.impl;

import org.crok4it.em.domain.Venue;
import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.repository.VenueRepository;
import org.crok4it.em.service.VenueService;
import org.crok4it.em.service.impl.VenueServiceImpl;
import org.crok4it.em.service.mapper.VenueMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DirtiesContext(classMode= BEFORE_CLASS)
public class VenueServiceImplTest {

    VenueMapper venueMapper;
    VenueRepository venueRepository;
    VenueService venueService;

    Method method;


    @BeforeEach
    void setUp() {
        venueMapper = mock(VenueMapper.class);
        venueRepository = mock(VenueRepository.class);
        venueService = new VenueServiceImpl(venueMapper, venueRepository);
        method = mock(Method.class);
    }

    @AfterEach
    void tearDown() {
        clearAllCaches();
    }


    @Test
    @DisplayName("Create venue with valid data")
    void createVenueShouldSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        UUID venueId = UUID.randomUUID();
        VenueDTO venueDto = mock(VenueDTO.class);
        Venue venue = mock(Venue.class);
        String phone = "Venue_create_phone";

        when(venueMapper.toEntity(venueDto)).thenReturn(venue);

        when(venueDto.getPhone()).thenReturn(phone);
        when(method.invoke(venueService, "findByPhone")).thenReturn(null);
        when(venueRepository.findByPhone(phone)).thenReturn(Optional.empty());

        when(venueRepository.saveAndFlush(venue)).thenReturn(venue);
        when(venue.getId()).thenReturn(venueId.toString());

        assertThat(venueService.createVenue(venueDto)).isEqualTo(venueId);

        InOrder inOrder = inOrder(venueMapper, venueRepository);
        //inOrder.verify(venueRepository).findByPhone(phone);
        inOrder.verify(venueMapper).toEntity(venueDto);
        inOrder.verify(venueRepository).saveAndFlush(venue);

    }

    @Test
    @DisplayName("Create venue with duplicate phone number will fail")
    void createVenueWithDuplicatePhoneNumberShouldFail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        UUID venueId = UUID.randomUUID();
        VenueDTO venueDto = mock(VenueDTO.class);
        Venue venue = mock(Venue.class);
        String phone = "Venue_create_phone";

        when(venueMapper.toEntity(venueDto)).thenReturn(venue);

        when(venueDto.getPhone()).thenReturn(phone);
        when(method.invoke(venueService, "findByPhone")).thenReturn(venue);
        when(venueRepository.findByPhone(phone)).thenReturn(Optional.of(venue));

        when(venueRepository.saveAndFlush(venue)).thenThrow(ConflictException.class);

        assertThatThrownBy(() -> venueService.createVenue(venueDto))
                .isInstanceOf(ConflictException.class)
                .hasMessage(String.format("Phone number %s already used. Please change", phone));

        InOrder inOrder = inOrder(venueRepository);
        inOrder.verify(venueRepository).findByPhone(phone);

    }

    @Test
    @DisplayName("Fetch venue by id from database")
    void findVenueByExistingIdShouldSuccess() {
        String venueId = UUID.randomUUID().toString();
        Venue venue = mock(Venue.class);
        VenueDTO venueDTO = mock(VenueDTO.class);

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));
        when(venueMapper.toDto(venue)).thenReturn(venueDTO);

        assertThat(venueService.findById(venueId)).isEqualTo(venueDTO);

        InOrder inOrder = inOrder(venueRepository, venueMapper);
        inOrder.verify(venueRepository).findById(venueId);
        inOrder.verify(venueMapper).toDto(venue);

    }
    @Test
    @DisplayName("Fetch venue with wrong id from database")
    void findVenueWithWrongIdShouldFail() {
        String venueId = UUID.randomUUID().toString();

        when(venueRepository.findById(venueId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> venueService.findById(venueId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Venue with id %s not found", venueId));

        InOrder inOrder = inOrder(venueRepository);
        inOrder.verify(venueRepository).findById(venueId);

    }

    @Test
    @DisplayName("Fetch venue by name from database")
    void findVenueByExistingNameShouldSuccess() {
        String name = "name";
        Venue venue = mock(Venue.class);
        VenueDTO venueDTO = mock(VenueDTO.class);

        when(venueRepository.findByNameContainsIgnoreCase(name)).thenReturn(Collections.singletonList(venue));
        when(venueMapper.toDto(venue)).thenReturn(venueDTO);

        assertThat(venueService.findByName(name)).isEqualTo(Collections.singletonList(venueDTO));

        InOrder inOrder = inOrder(venueRepository, venueMapper);
        inOrder.verify(venueRepository).findByNameContainsIgnoreCase(name);
        inOrder.verify(venueMapper).toDto(venue);

    }

    @Test
    @DisplayName("Fetch all venue from database")
    void findVenueAllExistingShouldSuccess() {

        Venue venue = mock(Venue.class);
        VenueDTO venueDTO = mock(VenueDTO.class);

        when(venueRepository.findAll()).thenReturn(Collections.singletonList(venue));
        when(venueMapper.toDto(venue)).thenReturn(venueDTO);

        assertThat(venueService.findAll()).isEqualTo(Collections.singletonList(venueDTO));

        InOrder inOrder = inOrder(venueRepository, venueMapper);
        inOrder.verify(venueRepository).findAll();
        inOrder.verify(venueMapper).toDto(venue);

    }

    @Test
    @DisplayName("Delete venue by id from database")
    void deleteVenueByExistingIdShouldSuccess() {
        String venueId = UUID.randomUUID().toString();
        Venue venue = mock(Venue.class);

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));
        doNothing().when(venueRepository).deleteById(venueId);

        venueService.deleteById(venueId);

        InOrder inOrder = inOrder(venueRepository);
        inOrder.verify(venueRepository).findById(venueId);
        inOrder.verify(venueRepository).deleteById(venueId);



    }
    @Test
    @DisplayName("Delete venue with wrong id from database")
    void deleteVenueWithWrongIdShouldFail() {
        String venueId = UUID.randomUUID().toString();
        Venue venue = mock(Venue.class);

        when(venueRepository.findById(venueId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> venueService.deleteById(venueId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Venue with id %s not found", venueId));

        InOrder inOrder = inOrder(venueRepository);
        inOrder.verify(venueRepository).findById(venueId);

    }

    @Test
    @DisplayName("Update venue by id from database")
    void updateVenueByExistingIdShouldSuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String venueId = UUID.randomUUID().toString();
        Venue venue = mock(Venue.class);
        VenueDTO venueDTO = mock(VenueDTO.class);
        String phone = "Venue_create_phone";

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(venue));

        when(venueDTO.getPhone()).thenReturn(phone);
        when(method.invoke(venueService, "findByPhone")).thenReturn(null);
        when(venueRepository.findByPhone(phone)).thenReturn(Optional.empty());

        when(venueMapper.fromDTOForUpdate(venue, venueDTO)).thenReturn(venue);
        when(venueRepository.saveAndFlush(venue)).thenReturn(venue);
        when(venueMapper.toDto(venue)).thenReturn(venueDTO);

        assertThat(venueService.update(venueId, venueDTO)).isEqualTo(venueDTO);

        InOrder inOrder = inOrder(venueRepository, venueMapper);
        inOrder.verify(venueRepository).findById(venueId);
        inOrder.verify(venueMapper).fromDTOForUpdate(venue, venueDTO);
        inOrder.verify(venueRepository).saveAndFlush(venue);



    }

    @Test
    @DisplayName("Update venue by wrong id from database")
    void updateVenueByWrongIdShouldFail() {
        String venueId = UUID.randomUUID().toString();
        VenueDTO venueDTO = mock(VenueDTO.class);

        when(venueRepository.findById(venueId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> venueService.update(venueId, venueDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Venue with id %s not found", venueId));

        InOrder inOrder = inOrder(venueRepository, venueMapper);
        inOrder.verify(venueRepository).findById(venueId);



    }

}
