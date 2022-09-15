package org.crok4it.em.service;

import org.crok4it.em.dto.VenueDTO;

import java.util.List;
import java.util.UUID;

public interface VenueService {
    UUID createVenue(VenueDTO venueDTO) throws NoSuchMethodException;

    VenueDTO findById(String toString);

    List<VenueDTO> findByName(String name);

    List<VenueDTO> findAll();

    void deleteById(String toString);
}
