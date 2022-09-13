package org.crok4it.em.service;

import org.crok4it.em.dto.VenueDTO;

import java.util.UUID;

public interface VenueService {
    UUID createVenue(VenueDTO venueDTO) throws NoSuchMethodException;
}
