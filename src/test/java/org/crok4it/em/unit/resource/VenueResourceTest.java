package org.crok4it.em.unit.resource;


import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.exception.ConflictException;
import org.crok4it.em.exception.ResourceNotFoundException;
import org.crok4it.em.resource.VenueResource;
import org.crok4it.em.service.impl.VenueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.crok4it.em.constant.VenueConstant.API_VENUE_BASE_ROUTE;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VenueResource.class)
public class VenueResourceTest extends BaseResourceTest{

    @MockBean
    private VenueServiceImpl venueService;

    @Autowired
    MockMvc mvc;

    private VenueDTO venueDTO;

    @BeforeEach
    void setup() {
        venueDTO = new VenueDTO()
                .name("Venue_Name")
                .city("Venue_City")
                .phone("Venue_Phone")
                .imageLink("http://Venue_image_link.com")
                .facebookLink("http://Venue_facebook_link.com")
                .websiteLink("http://Venue_website_link.com")
                .seekingTalent(true)
                .seekingDescription("Venue_seeking_description");

    }

    @Test
    @DisplayName("Create Venue With valid data")
    void createVenueWithValidDataShouldSuccess() throws Exception {
        UUID venueId = UUID.randomUUID();
        String message = "Venue created successfully";

        when(venueService.createVenue(venueDTO)).thenReturn(venueId);

        mvc.perform(post(API_VENUE_BASE_ROUTE)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
            .content(BaseResourceTest.asJsonString(venueDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.result").isArray())
            .andExpect(jsonPath("$.result[0]").value(venueId.toString()))
            .andExpect(jsonPath("$.message").value(message))
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Create venue without required data")
    void createVenueWithoutRequiredDataShouldFail() throws Exception {
        venueDTO
                .name("")
                .phone(null)
                .city("");

        mvc.perform(post(API_VENUE_BASE_ROUTE)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(BaseResourceTest.asJsonString(venueDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Create venue with facebook link already store in database")
    void createVenueWithDuplicateFacebookLinkShouldFail() throws Exception {

        when(venueService.createVenue(venueDTO)).thenThrow(ConflictException.class);

        mvc.perform(post(API_VENUE_BASE_ROUTE)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(BaseResourceTest.asJsonString(venueDTO)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Fetch venue by existing id from database")
    void fetchVenueByExistingIdShouldSuccess() throws Exception {
        UUID venueId = UUID.randomUUID();
        String message = "Venue fetch successfully";

        when(venueService.findById(venueId.toString())).thenReturn(venueDTO);

        mvc.perform(get(API_VENUE_BASE_ROUTE + "/" + venueId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").value(venueDTO))
                .andExpect(jsonPath("$.result[0].name").value(venueDTO.getName()))
                .andExpect(jsonPath("$.result[0].phone").value(venueDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Fetch venue with wrong id from database")
    void fetchVenueWithWrongIdShouldFail() throws Exception {
        UUID venueId = UUID.randomUUID();

        when(venueService.findById(venueId.toString())).thenThrow(ResourceNotFoundException.class);

        mvc.perform(get(API_VENUE_BASE_ROUTE + "/" + venueId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Fetch venue by existing name from database")
    void fetchVenueByExistingNameShouldSuccess() throws Exception {
        String name = "Venue_fetchByName_name";
        String message = "Venues fetch successfully";

        when(venueService.findByName(name)).thenReturn(Collections.singletonList(venueDTO));

        mvc.perform(get(API_VENUE_BASE_ROUTE + "/name/" + name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0][0].name").value(venueDTO.getName()))
                .andExpect(jsonPath("$.result[0][0].phone").value(venueDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Fetch all venue from database")
    void fetchVenueAllShouldSuccess() throws Exception {
        String message = "Venues fetch successfully";

        when(venueService.findAll()).thenReturn(Collections.singletonList(venueDTO));

        mvc.perform(get(API_VENUE_BASE_ROUTE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0]").isArray())
                .andExpect(jsonPath("$.result[0][0].name").value(venueDTO.getName()))
                .andExpect(jsonPath("$.result[0][0].phone").value(venueDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    /*@Test
    @DisplayName("Delete venue by existing id from database")
    void deleteVenueByExistingIdShouldSuccess() throws Exception {
        UUID venueId = UUID.randomUUID();
        String message = "Venue deleted successfully";

        doNothing().when(artisService).deleteById(venueId.toString());

        mvc.perform(delete(API_VENUE_BASE_ROUTE + "/" + venueId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Delete venue with wrong id from database")
    void deleteVenueWithWrongIdShouldFail() throws Exception {
        UUID venueId = UUID.randomUUID();

        doThrow(ResourceNotFoundException.class).when(artisService).deleteById(venueId.toString());


        mvc.perform(delete(API_VENUE_BASE_ROUTE + "/" + venueId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update venue by existing id from database")
    void updateVenueByExistingIdShouldSuccess() throws Exception {
        UUID venueId = UUID.randomUUID();
        String message = "Venue updated successfully";

        when(artisService.update(venueId.toString(), venueDTO)).thenReturn(venueDTO);

        mvc.perform(put(API_VENUE_BASE_ROUTE + "/" + venueId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(BaseResourceTest.asJsonString(venueDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").value(venueDTO))
                .andExpect(jsonPath("$.result[0].name").value(venueDTO.getName()))
                .andExpect(jsonPath("$.result[0].phone").value(venueDTO.getPhone()))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Update venue by existing id from database")
    void updateVenueByWrongIdShouldFail() throws Exception {
        UUID venueId = UUID.randomUUID();

        when(artisService.update(venueId.toString(), venueDTO)).thenThrow(ResourceNotFoundException.class);

        mvc.perform(put(API_VENUE_BASE_ROUTE + "/" + venueId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(BaseResourceTest.asJsonString(venueDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }*/



}
