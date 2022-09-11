package org.crok4it.em.service;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.dto.ArtistDTO;

import java.util.UUID;

public interface ArtistService {
    UUID createArtist(ArtistDTO artistDTO) throws NoSuchMethodException;

    ArtistDTO findById(String id);
    /*Artist findByPhone(String phone);
    Artist findByFacebookLink(String facebookLink);
    Artist findByImageLink(String imageLink);
    Artist findByWebsiteLink(String websiteLink);*/
}
