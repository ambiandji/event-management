package org.crok4it.em.repository;

import org.crok4it.em.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, String> {
    Optional<Venue> findByPhone(String phone);
    Optional<Venue> findByFacebookLinkIgnoreCase(String facebookLink);
    Optional<Venue> findByImageLinkIgnoreCase(String ImageLink);
    Optional<Venue> findByWebsiteLinkIgnoreCase(String WebsiteLink);

    List<Venue> findByNameContainsIgnoreCase(String name);
}
