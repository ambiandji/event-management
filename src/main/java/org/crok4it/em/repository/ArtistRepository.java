package org.crok4it.em.repository;

import org.crok4it.em.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, String> {
    Optional<Artist> findByPhone(String phone);
    Optional<Artist> findByFacebookLinkIgnoreCase(String facebookLink);
    Optional<Artist> findByImageLinkIgnoreCase(String ImageLink);
    Optional<Artist> findByWebsiteLinkIgnoreCase(String WebsiteLink);
}
