package org.crok4it.em.service;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;

import java.util.List;
import java.util.UUID;

public interface ArtistService {
    UUID createArtist(ArtistDTO artistDTO) throws NoSuchMethodException;

    ArtistDTO findById(String id);

    List<ArtistDTO> findByName(String name);

    List<ArtistDTO> findAll();
    /*Artist findByPhone(String phone);
    Artist findByFacebookLink(String facebookLink);
    Artist findByImageLink(String imageLink);
    Artist findByWebsiteLink(String websiteLink);*/
}
