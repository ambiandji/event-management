package org.crok4it.em.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crok4it.em.domain.Venue;
import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ErrorCode;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.repository.VenueRepository;
import org.crok4it.em.service.VenueService;
import org.crok4it.em.service.mapper.VenueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("venueService")
@Transactional
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueMapper venueMapper;
    private final VenueRepository venueRepository;
    @Override
    public UUID createVenue(VenueDTO venueDTO) throws NoSuchMethodException {
        validateVenueDTO(null, venueDTO);
        Venue venue = venueMapper.toEntity(venueDTO);
        venue = venueRepository.saveAndFlush(venue);
        return UUID.fromString(venue.getId());
    }

    @Override
    public VenueDTO findById(String id) {
        return venueRepository.findById(id)
                .map(venueMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Venue with id %s not found", id), ErrorCode.VENUE_NOT_FOUND));
    }

    @Override
    public List<VenueDTO> findByName(String name) {
        return venueRepository.findByNameContainsIgnoreCase(name)
                .stream()
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VenueDTO> findAll() {
        return venueRepository.findAll()
                .stream()
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
    }

    public Venue findByPhone(String phone) {
        return venueRepository.findByPhone(phone).orElse(null);
    }


    public Venue findByFacebookLink(String facebookLink) {
        return venueRepository.findByFacebookLinkIgnoreCase(facebookLink).orElse(null);
    }

    public Venue findByImageLink(String imageLink) {
        return venueRepository.findByImageLinkIgnoreCase(imageLink).orElse(null);
    }

    public Venue findByWebsiteLink(String websiteLink) {
        return venueRepository.findByWebsiteLinkIgnoreCase(websiteLink).orElse(null);
    }

    private void validateVenueDTO(String id, VenueDTO venueDTO) throws NoSuchMethodException {
        // verify if phone number is unique
        validatePhone(id, venueDTO.getPhone());
        // verify if facebook link number is unique
        if (venueDTO.getFacebookLink() != null && !venueDTO.getFacebookLink().isEmpty())
            validateFacebookLink(id, venueDTO.getFacebookLink());
        // verify if image link number is unique
        if(venueDTO.getImageLink() != null && !venueDTO.getImageLink().isEmpty())
            validateImageLink(id, venueDTO.getImageLink());
        // verify if website link number is unique
        if(venueDTO.getWebsiteLink() != null && !venueDTO.getWebsiteLink().isEmpty())
            validateWebsiteLink(id, venueDTO.getWebsiteLink());
    }

    private void validateWebsiteLink(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByWebsiteLink", value)) {
            log.error("Website link {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Website link %s already used. Please change", value), ErrorCode.VENUE_CONFLICT);
        }
    }

    private void validateImageLink(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByImageLink", value)) {
            log.error("Image link {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Image link %s already used. Please change", value), ErrorCode.VENUE_CONFLICT);
        }
    }

    private void validateFacebookLink(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByFacebookLink", value)) {
            log.error("Facebook link {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Facebook link %s already used. Please change", value), ErrorCode.VENUE_CONFLICT);
        }

    }

    private void validatePhone(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByPhone", value)) {
            log.error("Phone number {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Phone number %s already used. Please change", value), ErrorCode.VENUE_CONFLICT);
        }
    }

    private Boolean checkIfFieldIsUnique(String id, String methodName, String value) throws NoSuchMethodException {

        Method method = this.getClass().getMethod(methodName, String.class);
        try {
            Venue venue = (Venue) method.invoke(this, value);
            if (venue == null)
                return false;
            if (id == null || id.isEmpty()) {
                return true;
            } else {
                return !venue.getId().equals(id);
            }
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            System.out.println(e.getCause());
            return false;
        }

    }
}
