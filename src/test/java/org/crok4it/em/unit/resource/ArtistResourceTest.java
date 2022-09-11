package org.crok4it.em.unit.resource;


import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.resource.ArtisteResource;
import org.crok4it.em.service.impl.ArtistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.crok4it.em.constant.ArtistConstant.API_ARTIST_BASE_ROUTE;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtisteResource.class)
public class ArtistResourceTest extends BaseResourceTest{

    @MockBean
    private ArtistServiceImpl artisService;

    @Autowired
    MockMvc mvc;

    private ArtistDTO artistDTO;

    @BeforeEach
    void setup() {
        artistDTO = new ArtistDTO()
                .name("Artist_Name")
                .city("Artist_City")
                .phone("Artist_Phone")
                .imageLink("http://Artist_image_link.com")
                .facebookLink("http://Artist_facebook_link.com")
                .websiteLink("http://Artist_website_link.com")
                .seekingVenue(true)
                .seekingDescription("Artist_seeking_description");

    }

    @Test
    @DisplayName("Create Artist With valid data")
    void createArtistWithValidDataShouldSuccess() throws Exception {
        UUID artistId = UUID.randomUUID();
        String message = "Artist created successfully";

        when(artisService.createArtist(artistDTO)).thenReturn(artistId);

        mvc.perform(post(API_ARTIST_BASE_ROUTE)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
            .content(BaseResourceTest.asJsonString(artistDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.result").isArray())
            .andExpect(jsonPath("$.result[0]").value(artistId.toString()))
            .andExpect(jsonPath("$.message").value(message))
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Create artist without required data")
    void createArtistWithoutRequiredDataShouldFail() throws Exception {
        artistDTO
                .name("")
                .phone(null)
                .city("");

        mvc.perform(post(API_ARTIST_BASE_ROUTE)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(BaseResourceTest.asJsonString(artistDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Create artist with facebook link already store in database")
    void createArtistWithDuplicateFacebookLinkShouldFail() throws Exception {

        when(artisService.createArtist(artistDTO)).thenThrow(ConflictException.class);

        mvc.perform(post(API_ARTIST_BASE_ROUTE)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(BaseResourceTest.asJsonString(artistDTO)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Fetch artist by existing id from database")
    void fetchArtistByExistingIdShouldSuccess() throws Exception {
        UUID artistId = UUID.randomUUID();
        String message = "Artist fetch successfully";

        when(artisService.findById(artistId.toString())).thenReturn(artistDTO);

        mvc.perform(get(API_ARTIST_BASE_ROUTE + "/" + artistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").value(artistDTO))
                .andExpect(jsonPath("$.result[0].name").value(artistDTO.getName()))
                .andExpect(jsonPath("$.result[0].phone").value(artistDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Fetch artist with wrong id from database")
    void fetchArtistWithWrongIdShouldFail() throws Exception {
        UUID artistId = UUID.randomUUID();

        when(artisService.findById(artistId.toString())).thenThrow(ResourceNotFoundException.class);

        mvc.perform(get(API_ARTIST_BASE_ROUTE + "/" + artistId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fetch artist by existing name from database")
    void fetchArtistByExistingNameShouldSuccess() throws Exception {
        String name = "Artist_fetchByName_name";
        String message = "Artists fetch successfully";

        when(artisService.findByName(name)).thenReturn(Collections.singletonList(artistDTO));

        mvc.perform(get(API_ARTIST_BASE_ROUTE + "/name/" + name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0][0].name").value(artistDTO.getName()))
                .andExpect(jsonPath("$.result[0][0].phone").value(artistDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Fetch all artist from database")
    void fetchArtistAllShouldSuccess() throws Exception {
        String message = "Artists fetch successfully";

        when(artisService.findAll()).thenReturn(Collections.singletonList(artistDTO));

        mvc.perform(get(API_ARTIST_BASE_ROUTE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0][0].name").value(artistDTO.getName()))
                .andExpect(jsonPath("$.result[0][0].phone").value(artistDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Delete artist by existing id from database")
    void deleteArtistByExistingIdShouldSuccess() throws Exception {
        UUID artistId = UUID.randomUUID();
        String message = "Artist deleted successfully";

        doNothing().when(artisService).deleteById(artistId.toString());

        mvc.perform(delete(API_ARTIST_BASE_ROUTE + "/" + artistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Delete artist with wrong id from database")
    void deleteArtistWithWrongIdShouldFail() throws Exception {
        UUID artistId = UUID.randomUUID();

        doThrow(ResourceNotFoundException.class).when(artisService).deleteById(artistId.toString());


        mvc.perform(delete(API_ARTIST_BASE_ROUTE + "/" + artistId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
