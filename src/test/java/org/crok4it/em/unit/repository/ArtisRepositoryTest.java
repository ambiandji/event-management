package org.crok4it.em.unit.repository;

import org.crok4it.em.domain.Artist;
import org.crok4it.em.repository.ArtistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ArtisRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("CRUD Test")
    void crudTest() {
        String artistId = createTest();
        updateTest(artistId);
        findById(artistId);
        findAll(artistId);
        deleteById(artistId);
    }

    private void deleteById(String artistId) {

        String artistId1 = "6a12e5b6-9397-4e7f-8fea-83a0e1c75277";
        String artistId2 = "22b07a69-bfc7-4218-a9c9-c890fa6beb3c";

        artistRepository.deleteById(artistId);

        assertThat(artistRepository.findAll())
                .hasSize(6)
                .map(Artist::getId)
                .doesNotContain(artistId)
                .contains(artistId1, artistId2);
    }

    private void findById(String artistId) {
        String updateName = "Artist_update_name";
        String updateCity = "Artist_update_city";
        String updatePhone = "Artist_update_phone";
        String facebookLink = "https://Artist_create_facebookLink.com";
        String imageLink = "https://Artist_create_imageLink.com";
        String websiteLink = "https://Artist_create_websiteLink.com";
        Boolean seekingVenue = true;
        String seekingDescription = "Artist_create_seeking_description";

        assertThat(artistRepository.findById(artistId))
                .isNotEmpty()
                .hasValueSatisfying(artist -> assertThat(artist)
                        .returns(updateName, Artist::getName)
                        .returns(updatePhone, Artist::getPhone)
                        .returns(updateCity, Artist::getCity)
                        .returns(facebookLink, Artist::getFacebookLink)
                        .returns(imageLink, Artist::getImageLink)
                        .returns(websiteLink, Artist::getWebsiteLink)
                        .returns(seekingVenue, Artist::getSeekingVenue)
                        .returns(seekingDescription, Artist::getSeekingDescription)
                );
    }

    private void findAll(String artistId) {
        String artistId1 = "6a12e5b6-9397-4e7f-8fea-83a0e1c75277";
        String artistId2 = "22b07a69-bfc7-4218-a9c9-c890fa6beb3c";

        assertThat(jdbcTemplate.queryForList("SELECT * FROM t_artist"))
                .map(stringObjectMap -> stringObjectMap.get("c_id"))
                .contains(artistId, artistId1, artistId2);

        assertThat(artistRepository.findAll())
                .hasSize(7)
                .map(Artist::getId)
                .contains(artistId, artistId1, artistId2);
    }

    private void updateTest(String artistId) {
        String updateName = "Artist_update_name";
        String updateCity = "Artist_update_city";
        String updatePhone = "Artist_update_phone";

        Artist artistInDb = artistRepository.getReferenceById(artistId);

        artistInDb.setName(updateName);
        artistInDb.setCity(updateCity);
        artistInDb.setPhone(updatePhone);

        artistRepository.saveAndFlush(artistInDb);

        assertThat(artistRepository.findById(artistId))
                .isNotEmpty()
                .hasValueSatisfying(updatedArtist -> assertThat(updatedArtist)
                        .returns(updateName, Artist::getName)
                        .returns(updateCity, Artist::getCity)
                        .returns(updatePhone, Artist::getPhone));
    }

    private String createTest() {
        Artist artist = Artist.builder()
                .name("Artist_create_name")
                .phone("Artist_create_phone")
                .city("Artist_create_city")
                .facebookLink("https://Artist_create_facebookLink.com")
                .imageLink("https://Artist_create_imageLink.com")
                .websiteLink("https://Artist_create_websiteLink.com")
                .seekingVenue(true)
                .seekingDescription("Artist_create_seeking_description")
                .build();

        Artist savedArtist = artistRepository.saveAndFlush(artist);

        assertThat(savedArtist)
                .isNotNull()
                .satisfies(a -> {
                    assertThat(a.getId()).isNotNull();
                    assertThat(a.getName()).isEqualTo(artist.getName());
                    assertThat(a.getCity()).isEqualTo(artist.getCity());
                    assertThat(a.getPhone()).isEqualTo(artist.getPhone());
                    assertThat(a.getFacebookLink()).isEqualTo(artist.getFacebookLink());
                    assertThat(a.getImageLink()).isEqualTo(artist.getImageLink());
                    assertThat(a.getWebsiteLink()).isEqualTo(artist.getWebsiteLink());
                    assertThat(a.getSeekingVenue()).isTrue();
                    assertThat(a.getSeekingDescription()).isEqualTo(artist.getSeekingDescription());
                });
        return savedArtist.getId();
    }

    @Test
    @DisplayName("find Artist by phone")
    void findArtistByPhone() {
        String phone = "Artist_findByPhone_phone";

        String id = "19440509-759c-42cd-bc4e-168df7151d89";
        String name = "Artist_findByPhone_name";
        String city = "Artist_findByPhone_city";
        String facebookLink = "http://Artist_findByPhone_facebook_link";
        String imageLink = "http://Artist_findByPhone_image_link";
        String websiteLink = "http://Artist_findByPhone_website_link";
        Boolean seekingVenue = true;
        String seekingDescription = "Artist_findByPhone_seeking_description";

        assertThat(artistRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(artist -> assertThat(artist)
                        .returns(id, Artist::getId)
                        .returns(name, Artist::getName)
                        .returns(city, Artist::getCity)
                        .returns(facebookLink, Artist::getFacebookLink)
                        .returns(imageLink, Artist::getImageLink)
                        .returns(websiteLink, Artist::getWebsiteLink)
                        .returns(seekingVenue, Artist::getSeekingVenue)
                        .returns(seekingDescription, Artist::getSeekingDescription)
                );
    }

    @Test
    @DisplayName("find Artist by facebook link")
    void findArtistByFacebookLink() {
        String facebookLink = "http://Artist_findByFacebookLink_facebook_link";

        String id = "885a9e83-cb9c-4e62-817a-fd1653489710";
        String name = "Artist_findByFacebookLink_name";
        String phone = "Artist_findByFacebookLink_phone";
        String city = "Artist_findByFacebookLink_city";
        String imageLink = "http://Artist_findByFacebookLink_image_link";
        String websiteLink = "http://Artist_findByFacebookLink_website_link";
        Boolean seekingVenue = true;
        String seekingDescription = "Artist_findByFacebookLink_seeking_description";

        assertThat(artistRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(artist -> assertThat(artist)
                        .returns(id, Artist::getId)
                        .returns(name, Artist::getName)
                        .returns(city, Artist::getCity)
                        .returns(facebookLink, Artist::getFacebookLink)
                        .returns(imageLink, Artist::getImageLink)
                        .returns(websiteLink, Artist::getWebsiteLink)
                        .returns(seekingVenue, Artist::getSeekingVenue)
                        .returns(seekingDescription, Artist::getSeekingDescription)
                );
    }


    @Test
    @DisplayName("find Artist by image link")
    void findArtistByImageLink() {
        String facebookLink = "http://Artist_findByImageLink_facebook_link";

        String id = "f42160ff-fdd7-41f2-a0cb-521f7e0e128c";
        String name = "Artist_findByImageLink_name";
        String phone = "Artist_findByImageLink_phone";
        String city = "Artist_findByImageLink_city";
        String imageLink = "http://Artist_findByImageLink_image_link";
        String websiteLink = "http://Artist_findByImageLink_website_link";
        Boolean seekingVenue = true;
        String seekingDescription = "Artist_findByImageLink_seeking_description";

        assertThat(artistRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(artist -> assertThat(artist)
                        .returns(id, Artist::getId)
                        .returns(name, Artist::getName)
                        .returns(city, Artist::getCity)
                        .returns(facebookLink, Artist::getFacebookLink)
                        .returns(imageLink, Artist::getImageLink)
                        .returns(websiteLink, Artist::getWebsiteLink)
                        .returns(seekingVenue, Artist::getSeekingVenue)
                        .returns(seekingDescription, Artist::getSeekingDescription)
                );
    }

    @Test
    @DisplayName("find Artist by website link")
    void findArtistByWebsiteLink() {
        String websiteLink = "http://Artist_findByWebsiteLink_website_link";

        String id = "4c5266e7-6159-4426-b5ea-163b243e191a";
        String name = "Artist_findByWebsiteLink_name";
        String phone = "Artist_findByWebsiteLink_phone";
        String city = "Artist_findByWebsiteLink_city";
        String facebookLink = "http://Artist_findByWebsiteLink_facebook_link";
        String imageLink = "http://Artist_findByWebsiteLink_image_link";
        Boolean seekingVenue = true;
        String seekingDescription = "Artist_findByWebsiteLink_seeking_description";

        assertThat(artistRepository.findByPhone(phone))
                .isNotEmpty()
                .hasValueSatisfying(artist -> assertThat(artist)
                        .returns(id, Artist::getId)
                        .returns(name, Artist::getName)
                        .returns(city, Artist::getCity)
                        .returns(facebookLink, Artist::getFacebookLink)
                        .returns(imageLink, Artist::getImageLink)
                        .returns(websiteLink, Artist::getWebsiteLink)
                        .returns(seekingVenue, Artist::getSeekingVenue)
                        .returns(seekingDescription, Artist::getSeekingDescription)
                );
    }
}
