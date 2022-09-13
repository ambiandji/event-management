package org.crok4it.em.unit.repository;

import org.crok4it.em.domain.Venue;
import org.crok4it.em.repository.VenueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class VenueRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("CRUD Test")
    void crudTest() {
        String venueId = createTest();
        updateTest(venueId);
        findById(venueId);
        findAll(venueId);
        deleteById(venueId);
    }

    private void deleteById(String venueId) {

        String venueId1 = "6a12e5b6-9397-4e7f-8fea-83a0e1c75277";
        String venueId2 = "22b07a69-bfc7-4218-a9c9-c890fa6beb3c";

        venueRepository.deleteById(venueId);

        assertThat(venueRepository.findAll())
                .hasSize(6)
                .map(Venue::getId)
                .doesNotContain(venueId)
                .contains(venueId1, venueId2);
    }

    private void findById(String venueId) {
        String updateName = "Venue_update_name";
        String updateCity = "Venue_update_city";
        String updatePhone = "Venue_update_phone";
        String facebookLink = "https://Venue_create_facebookLink.com";
        String imageLink = "https://Venue_create_imageLink.com";
        String websiteLink = "https://Venue_create_websiteLink.com";
        Boolean seekingTalent = true;
        String seekingDescription = "Venue_create_seeking_description";

        assertThat(venueRepository.findById(venueId))
                .isNotEmpty()
                .hasValueSatisfying(venue -> assertThat(venue)
                        .returns(updateName, Venue::getName)
                        .returns(updatePhone, Venue::getPhone)
                        .returns(updateCity, Venue::getCity)
                        .returns(facebookLink, Venue::getFacebookLink)
                        .returns(imageLink, Venue::getImageLink)
                        .returns(websiteLink, Venue::getWebsiteLink)
                        .returns(seekingTalent, Venue::getSeekingTalent)
                        .returns(seekingDescription, Venue::getSeekingDescription)
                );
    }

    private void findAll(String venueId) {
        String venueId1 = "6a12e5b6-9397-4e7f-8fea-83a0e1c75277";
        String venueId2 = "22b07a69-bfc7-4218-a9c9-c890fa6beb3c";

        assertThat(jdbcTemplate.queryForList("SELECT * FROM t_venue"))
                .map(stringObjectMap -> stringObjectMap.get("c_id"))
                .contains(venueId, venueId1, venueId2);

        assertThat(venueRepository.findAll())
                .hasSize(7)
                .map(Venue::getId)
                .contains(venueId, venueId1, venueId2);
    }

    private void updateTest(String venueId) {
        String updateName = "Venue_update_name";
        String updateCity = "Venue_update_city";
        String updatePhone = "Venue_update_phone";

        Venue venueInDb = venueRepository.getReferenceById(venueId);

        venueInDb.setName(updateName);
        venueInDb.setCity(updateCity);
        venueInDb.setPhone(updatePhone);

        venueRepository.saveAndFlush(venueInDb);

        assertThat(venueRepository.findById(venueId))
                .isNotEmpty()
                .hasValueSatisfying(updatedVenue -> assertThat(updatedVenue)
                        .returns(updateName, Venue::getName)
                        .returns(updateCity, Venue::getCity)
                        .returns(updatePhone, Venue::getPhone));
    }

    private String createTest() {
        Venue venue = Venue.builder()
                .name("Venue_create_name")
                .phone("Venue_create_phone")
                .city("Venue_create_city")
                .facebookLink("https://Venue_create_facebookLink.com")
                .imageLink("https://Venue_create_imageLink.com")
                .websiteLink("https://Venue_create_websiteLink.com")
                .seekingTalent(true)
                .seekingDescription("Venue_create_seeking_description")
                .build();

        Venue savedVenue = venueRepository.saveAndFlush(venue);

        assertThat(savedVenue)
                .isNotNull()
                .satisfies(a -> {
                    assertThat(a.getId()).isNotNull();
                    assertThat(a.getName()).isEqualTo(venue.getName());
                    assertThat(a.getCity()).isEqualTo(venue.getCity());
                    assertThat(a.getPhone()).isEqualTo(venue.getPhone());
                    assertThat(a.getFacebookLink()).isEqualTo(venue.getFacebookLink());
                    assertThat(a.getImageLink()).isEqualTo(venue.getImageLink());
                    assertThat(a.getWebsiteLink()).isEqualTo(venue.getWebsiteLink());
                    assertThat(a.getSeekingTalent()).isTrue();
                    assertThat(a.getSeekingDescription()).isEqualTo(venue.getSeekingDescription());
                });
        return savedVenue.getId();
    }

    @Test
    @DisplayName("find Venue by phone")
    void findVenueByPhone() {
        String phone = "Venue_findByPhone_phone";

        String id = "19440509-759c-42cd-bc4e-168df7151d89";
        String name = "Venue_findByPhone_name";
        String city = "Venue_findByPhone_city";
        String facebookLink = "http://Venue_findByPhone_facebook_link";
        String imageLink = "http://Venue_findByPhone_image_link";
        String websiteLink = "http://Venue_findByPhone_website_link";
        Boolean seekingTalent = true;
        String seekingDescription = "Venue_findByPhone_seeking_description";

        assertThat(venueRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(venue -> assertThat(venue)
                        .returns(id, Venue::getId)
                        .returns(name, Venue::getName)
                        .returns(city, Venue::getCity)
                        .returns(facebookLink, Venue::getFacebookLink)
                        .returns(imageLink, Venue::getImageLink)
                        .returns(websiteLink, Venue::getWebsiteLink)
                        .returns(seekingTalent, Venue::getSeekingTalent)
                        .returns(seekingDescription, Venue::getSeekingDescription)
                );
    }

    @Test
    @DisplayName("find Venue by facebook link")
    void findVenueByFacebookLink() {
        String facebookLink = "http://Venue_findByFacebookLink_facebook_link";

        String id = "885a9e83-cb9c-4e62-817a-fd1653489710";
        String name = "Venue_findByFacebookLink_name";
        String phone = "Venue_findByFacebookLink_phone";
        String city = "Venue_findByFacebookLink_city";
        String imageLink = "http://Venue_findByFacebookLink_image_link";
        String websiteLink = "http://Venue_findByFacebookLink_website_link";
        Boolean seekingTalent = true;
        String seekingDescription = "Venue_findByFacebookLink_seeking_description";

        assertThat(venueRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(venue -> assertThat(venue)
                        .returns(id, Venue::getId)
                        .returns(name, Venue::getName)
                        .returns(city, Venue::getCity)
                        .returns(facebookLink, Venue::getFacebookLink)
                        .returns(imageLink, Venue::getImageLink)
                        .returns(websiteLink, Venue::getWebsiteLink)
                        .returns(seekingTalent, Venue::getSeekingTalent)
                        .returns(seekingDescription, Venue::getSeekingDescription)
                );
    }


    @Test
    @DisplayName("find Venue by image link")
    void findVenueByImageLink() {
        String facebookLink = "http://Venue_findByImageLink_facebook_link";

        String id = "f42160ff-fdd7-41f2-a0cb-521f7e0e128c";
        String name = "Venue_findByImageLink_name";
        String phone = "Venue_findByImageLink_phone";
        String city = "Venue_findByImageLink_city";
        String imageLink = "http://Venue_findByImageLink_image_link";
        String websiteLink = "http://Venue_findByImageLink_website_link";
        Boolean seekingTalent = true;
        String seekingDescription = "Venue_findByImageLink_seeking_description";

        assertThat(venueRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(venue -> assertThat(venue)
                        .returns(id, Venue::getId)
                        .returns(name, Venue::getName)
                        .returns(city, Venue::getCity)
                        .returns(facebookLink, Venue::getFacebookLink)
                        .returns(imageLink, Venue::getImageLink)
                        .returns(websiteLink, Venue::getWebsiteLink)
                        .returns(seekingTalent, Venue::getSeekingTalent)
                        .returns(seekingDescription, Venue::getSeekingDescription)
                );
    }

    @Test
    @DisplayName("find Venue by website link")
    void findVenueByWebsiteLink() {
        String websiteLink = "http://Venue_findByWebsiteLink_website_link";

        String id = "4c5266e7-6159-4426-b5ea-163b243e191a";
        String name = "Venue_findByWebsiteLink_name";
        String phone = "Venue_findByWebsiteLink_phone";
        String city = "Venue_findByWebsiteLink_city";
        String facebookLink = "http://Venue_findByWebsiteLink_facebook_link";
        String imageLink = "http://Venue_findByWebsiteLink_image_link";
        Boolean seekingTalent = true;
        String seekingDescription = "Venue_findByWebsiteLink_seeking_description";

        assertThat(venueRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(venue -> assertThat(venue)
                        .returns(id, Venue::getId)
                        .returns(name, Venue::getName)
                        .returns(city, Venue::getCity)
                        .returns(facebookLink, Venue::getFacebookLink)
                        .returns(imageLink, Venue::getImageLink)
                        .returns(websiteLink, Venue::getWebsiteLink)
                        .returns(seekingTalent, Venue::getSeekingTalent)
                        .returns(seekingDescription, Venue::getSeekingDescription)
                );
    }

    @Test
    @DisplayName("Find venue by name")
    void findByNameContainsIgnoreCaseExistingNameShouldSuccess() {
        String name = "name";
        venueRepository.findByNameContainsIgnoreCase(name).forEach(venue -> System.out.println(venue.getName()));
        assertThat(venueRepository.findByNameContainsIgnoreCase(name))
                .hasSizeGreaterThan(0)
                .isInstanceOf(ArrayList.class);

    }
}
