package org.crok4it.em.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ErrorCode;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.repository.ArtistRepository;
import org.crok4it.em.service.ArtistService;
import org.crok4it.em.service.mapper.ArtistMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("artistService")
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistMapper artistMapper;
    private final ArtistRepository artistRepository;

    @Override
    @Transactional
    public UUID createArtist(ArtistDTO artistDTO) throws NoSuchMethodException {
        validateArtistDTO(null, artistDTO);
        Artist artist = artistMapper.toEntity(artistDTO);
        artist = artistRepository.saveAndFlush(artist);
        return UUID.fromString(artist.getId());
    }

    @Override
    public ArtistDTO findById(String id) {
        return artistRepository.findById(id)
                .map(artistMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Artist with id %s not found", id), ErrorCode.ARTIST_NOT_FOUND));
    }

    @Override
    public List<ArtistDTO> findByName(String name) {
        return artistRepository.findByNameContainsIgnoreCase(name)
                .stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtistDTO> findAll() {
        return artistRepository.findAll()
                .stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        artistRepository.findById(id).orElseThrow(() -> {
            log.error("Artist with id {} not found", id);
            throw new ResourceNotFoundException(
                    String.format("Artist with id %s not found", id), ErrorCode.ARTIST_NOT_FOUND);
        });
        artistRepository.deleteById(id);
    }

    @Override
    public ArtistDTO update(String id, ArtistDTO artistDTO) throws NoSuchMethodException {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> {
            log.error("Artist with id {} not found", id);
            throw new ResourceNotFoundException(
                    String.format("Artist with id %s not found", id), ErrorCode.ARTIST_NOT_FOUND);
        });
       return artistMapper.toDto(artistRepository.saveAndFlush(artistMapper.fromDTOForUpdate(artist, artistDTO)));

    }

    public Artist findByPhone(String phone) {
        return artistRepository.findByPhone(phone).orElse(null);
    }


    public Artist findByFacebookLink(String facebookLink) {
        return artistRepository.findByFacebookLinkIgnoreCase(facebookLink).orElse(null);
    }

    public Artist findByImageLink(String imageLink) {
        return artistRepository.findByImageLinkIgnoreCase(imageLink).orElse(null);
    }

    public Artist findByWebsiteLink(String websiteLink) {
        return artistRepository.findByWebsiteLinkIgnoreCase(websiteLink).orElse(null);
    }

    private void validateArtistDTO(String id, ArtistDTO artistDTO) throws NoSuchMethodException {
        // verify if phone number is unique
        validatePhone(id, artistDTO.getPhone());
        // verify if facebook link number is unique
        if (artistDTO.getFacebookLink() != null && !artistDTO.getFacebookLink().isEmpty())
            validateFacebookLink(id, artistDTO.getFacebookLink());
        // verify if image link number is unique
        if(artistDTO.getImageLink() != null && !artistDTO.getImageLink().isEmpty())
            validateImageLink(id, artistDTO.getImageLink());
        // verify if website link number is unique
        if(artistDTO.getWebsiteLink() != null && !artistDTO.getWebsiteLink().isEmpty())
            validateWebsiteLink(id, artistDTO.getWebsiteLink());
    }

    private void validateWebsiteLink(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByWebsiteLink", value)) {
            log.error("Website link {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Website link %s already used. Please change", value), ErrorCode.ARTIST_CONFLICT);
        }
    }

    private void validateImageLink(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByImageLink", value)) {
            log.error("Image link {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Image link %s already used. Please change", value), ErrorCode.ARTIST_CONFLICT);
        }
    }

    private void validateFacebookLink(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByFacebookLink", value)) {
            log.error("Facebook link {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Facebook link %s already used. Please change", value), ErrorCode.ARTIST_CONFLICT);
        }

    }

    private void validatePhone(String id, String value) throws NoSuchMethodException {
        if(checkIfFieldIsUnique(id, "findByPhone", value)) {
            log.error("Phone number {} already used. Please change", value);
            throw new ConflictException(
                    String.format("Phone number %s already used. Please change", value), ErrorCode.ARTIST_CONFLICT);
        }
    }

    private Boolean checkIfFieldIsUnique(String id, String methodName, String value) throws NoSuchMethodException {

        Method  method = this.getClass().getMethod(methodName, String.class);
        try {
            Artist artist = (Artist) method.invoke(this, value);
            if (artist == null)
                return false;
            if (id == null || id.isEmpty()) {
                return true;
            } else {
                return !artist.getId().equals(id);
            }
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            System.out.println(e.getCause());
            return false;
        }

    }
}
