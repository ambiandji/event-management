package org.crok4it.em.e2e.steps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.e2e.mapper.VenueDTORowMapper;
import org.crok4it.em.unit.resource.BaseResourceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.crok4it.em.constant.VenueConstant.API_VENUE_BASE_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VenueSteps implements En {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mvc;

    private UUID venueId;

    private VenueDTO venueDTO;

    private List<VenueDTO> venueDTOS = new ArrayList<>();

    private String msg;

    public VenueSteps() {
        When("I create a new venue with the following data",
                (DataTable dataTable) -> {

                    VenueDTO venueDataTable = getVenueFromFeature(dataTable);

                    MvcResult result = mvc.perform(post(API_VENUE_BASE_ROUTE)
                                    .accept(APPLICATION_JSON)
                                    .contentType(APPLICATION_JSON)
                                    .content(BaseResourceTest.asJsonString(venueDataTable))
                            )
                            .andDo(print())
                            .andExpect(status().isCreated())
                            .andReturn();
                    venueId = UUID.fromString(JsonPath.read(result.getResponse().getContentAsString(), "$.result[0]"));

                    assertThat(venueId)
                            .isNotNull()
                            .isInstanceOf(UUID.class);

        });
        Then("I should see that the current venue identifier has the following data",
                (DataTable dataTable) -> {
                    Map<String, String> venueAsMap = dataTable.asMaps().get(0);

                    VenueDTO dbVenueDTO = jdbcTemplate.queryForObject(
                            String.format("SELECT * FROM t_venue WHERE c_id = '%s'", venueId.toString()),
                            new VenueDTORowMapper()
                    );

                    assertThat(dbVenueDTO)
                            .isNotNull()
                            .returns(venueAsMap.get("name"), VenueDTO::getName)
                            .returns(venueAsMap.get("phone"), VenueDTO::getPhone)
                            .returns(venueAsMap.get("city"), VenueDTO::getCity);

        });
        Then("I should see that attempt to create a new venue with the following data will fail with status error {string} and error code {string}",
                (String httpStatus, String  errorCode, DataTable dataTable) -> {
                    VenueDTO venueDataTable = getVenueFromFeature(dataTable);

                    MvcResult result = mvc.perform(post(API_VENUE_BASE_ROUTE)
                                    .accept(APPLICATION_JSON)
                                    .contentType(APPLICATION_JSON)
                                    .content(BaseResourceTest.asJsonString(venueDataTable))
                            )
                            .andDo(print())
                            .andReturn();

                    assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                    assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });
        And("Following venue is in database",
                (DataTable dataTable) -> {
                    assertThat(
                            dataTable
                                    .asMaps()
                                    .stream()
                                    .allMatch(this::isPresentInTheDatabase)
                    ).isTrue();

        });
        When("I fetch venue with id {string}",
                (String id) -> {
                    MvcResult result = mvc.perform(get(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();

                    venueDTO = parseJsonToObject(result);

                    assertThat(venueDTO)
                            .isNotNull()
                            .isInstanceOf(VenueDTO.class);

        });
        Then("I should the following venue is returned from database", (DataTable dataTable) -> {
            VenueDTO venueDataTable = getVenueFromFeature(dataTable);

            assertThat(venueDTO)
                    .isNotNull()
                    .returns(venueDataTable.getId(), VenueDTO::getId)
                    .returns(venueDataTable.getName(), VenueDTO::getName)
                    .returns(venueDataTable.getPhone(), VenueDTO::getPhone)
                    .returns(venueDataTable.getCity(), VenueDTO::getCity);

        });
        Then("The attempt to fetch an venue with the id {string} will fail with status error {string} and error code {string}",
                (String id, String httpStatus, String errorCode) -> {
                    MvcResult result = mvc.perform(get(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isNotFound())
                            .andReturn();
                    assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                    assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });
        When("I fetch venue with name {string}",
                (String venueName) -> {

                    MvcResult result = mvc.perform(get(API_VENUE_BASE_ROUTE + "/name/" + venueName)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();

                    venueDTOS = parseJsonToList(result);

                    assertThat(venueDTOS)
                            .hasSizeGreaterThan(0)
                            .isInstanceOf(ArrayList.class);

        });
        Then("The attempt to fetch an venue with the name {string} will return empty list",
                (String venueName) -> {

                    MvcResult result = mvc.perform(get(API_VENUE_BASE_ROUTE + "/name/" + venueName)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();

                    venueDTOS = parseJsonToList(result);

                    assertThat(venueDTOS)
                            .hasSize(0)
                            .isInstanceOf(ArrayList.class);

        });
        Then("The attempt to fetch all venue from database will return a list of venue",
                () -> {

                    MvcResult result = mvc.perform(get(API_VENUE_BASE_ROUTE)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();

                    venueDTOS = parseJsonToList(result);

                    assertThat(venueDTOS)
                            .hasSizeGreaterThan(0)
                            .isInstanceOf(ArrayList.class);
        });
        When("I delete venue with id {string}",
                ( String id) -> {
                    MvcResult result = mvc.perform(delete(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();


                    msg = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
        });
        Then("I should the see that the venue with id {string} is no longer in database and success message is {string}",
                (String id, String message) -> {

                    assertThat(msg).isEqualTo(message);

                    MvcResult result = mvc.perform(get(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isNotFound())
                            .andReturn();

        });
        Then("The attempt to delete an venue with the id {string} will fail with status error {string} and error code {string}",
                (String id, String httpStatus, String errorCode) -> {

                    MvcResult result = mvc.perform(delete(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isNotFound())
                            .andReturn();
                    assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                    assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });
        And("The following venue is store in database",
                (DataTable dataTable) -> {

                String id = dataTable.asMaps().get(0).get("id");

                Map<String, String> artistAsMap = dataTable.asMaps().get(0);

                VenueDTO dbVenueDTO = jdbcTemplate.queryForObject(
                        String.format("SELECT * FROM t_venue WHERE c_id = '%s'", id),
                        new VenueDTORowMapper()
                );

                assertThat(dbVenueDTO)
                        .isNotNull()
                        .returns(UUID.fromString(artistAsMap.get("id")), VenueDTO::getId)
                        .returns(artistAsMap.get("name"), VenueDTO::getName)
                        .returns(artistAsMap.get("phone"), VenueDTO::getPhone)
                        .returns(artistAsMap.get("city"), VenueDTO::getCity);

        });
        When("I update venue with id {string} with the following data",
                (String id, DataTable dataTable) -> {
                    VenueDTO venueDataTable = getVenueFromFeature(dataTable);

                    MvcResult result = mvc.perform(put(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON)
                                    .contentType(APPLICATION_JSON)
                                    .content(BaseResourceTest.asJsonString(venueDataTable))
                            )
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();
                    venueDTO = parseJsonToObject(result);

                    assertThat(venueDTO)
                            .isNotNull()
                            .isInstanceOf(VenueDTO.class);

        });
        Then("I should the following venue is saved to database",
                (DataTable dataTable) -> {
                    Map<String, String> venueAsMap = dataTable.asMaps().get(0);

                    assertThat(venueDTO)
                            .isNotNull()
                            .returns(UUID.fromString(venueAsMap.get("id")), VenueDTO::getId)
                            .returns(venueAsMap.get("name"), VenueDTO::getName)
                            .returns(venueAsMap.get("phone"), VenueDTO::getPhone)
                            .returns(venueAsMap.get("city"), VenueDTO::getCity);

        });
        Then("The attempt to update an venue with the id {string} and following data will fail with status error {string} and error code {string}",
                (String id, String httpStatus, String errorCode, DataTable dataTable) -> {
                    VenueDTO venueDataTable = getVenueFromFeature(dataTable);

                    MvcResult result = mvc.perform(put(API_VENUE_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON)
                                    .contentType(APPLICATION_JSON)
                                    .content(BaseResourceTest.asJsonString(venueDataTable))
                            )
                            .andDo(print())
                            .andExpect(status().isNotFound())
                            .andReturn();
                    assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                    assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });

    }

    private VenueDTO parseJsonToObject(MvcResult result) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        return gson.fromJson(String.valueOf((LinkedHashMap)
                JsonPath.read(result.getResponse().getContentAsString(), "$.result[0]")), VenueDTO.class);
    }

    private List<VenueDTO> parseJsonToList(MvcResult result) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        Type venueDTOListType = new TypeToken<ArrayList<ArtistDTO>>(){}.getType();
        return gson.fromJson(String.valueOf((ArrayList<LinkedHashMap>)
                JsonPath.read(result.getResponse().getContentAsString(), "$.result[0]")), venueDTOListType);
    }

    private boolean isPresentInTheDatabase(Map<String, String> dataMap) {
        assertThat(
                jdbcTemplate.queryForList(
                        String.format(
                                "SELECT * FROM t_venue WHERE c_id = '%s'",
                                dataMap.get("id")
                        )
                )
        ).isNotEmpty();
        return true;
    }

    private VenueDTO getVenueFromFeature(DataTable dataTable) {
        if (dataTable.asMaps().get(0).get("id") != null) {
            return venueDTO = new VenueDTO()
                    .id(UUID.fromString(dataTable.asMaps().get(0).get("id")))
                    .name(dataTable.asMaps().get(0).get("name"))
                    .phone(dataTable.asMaps().get(0).get("phone"))
                    .city(dataTable.asMaps().get(0).get("city"))
                    .facebookLink(dataTable.asMaps().get(0).get("facebookLink"))
                    .imageLink(dataTable.asMaps().get(0).get("imageLink"))
                    .websiteLink(dataTable.asMaps().get(0).get("websiteLink"))
                    .seekingTalent(Boolean.valueOf(dataTable.asMaps().get(0).get("seekingTalent")))
                    .seekingDescription(dataTable.asMaps().get(0).get("seekingDescription"));
        } else {
            return venueDTO = new VenueDTO()
                    .name(dataTable.asMaps().get(0).get("name"))
                    .phone(dataTable.asMaps().get(0).get("phone"))
                    .city(dataTable.asMaps().get(0).get("city"))
                    .facebookLink(dataTable.asMaps().get(0).get("facebookLink"))
                    .imageLink(dataTable.asMaps().get(0).get("imageLink"))
                    .websiteLink(dataTable.asMaps().get(0).get("websiteLink"))
                    .seekingTalent(Boolean.valueOf(dataTable.asMaps().get(0).get("seekingTalent")))
                    .seekingDescription(dataTable.asMaps().get(0).get("seekingDescription"));

        }
    }
}
