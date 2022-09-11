package org.crok4it.em.e2e.steps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.e2e.mapper.ArtistDTORowMapper;
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
import static org.crok4it.em.constant.ArtistConstant.API_ARTIST_BASE_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArtistSteps implements En{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mvc;

    private UUID artistId;

    private ArtistDTO artistDTO;

    private List<ArtistDTO> artistDTOS = new ArrayList<>();

    private String msg;

    public ArtistSteps() {

        When("I create a new artist with the following data",
                (DataTable dataTable) -> {
            
            ArtistDTO artistDataTable = getArtistFromFeature(dataTable);

            MvcResult result = mvc.perform(post(API_ARTIST_BASE_ROUTE)
                                    .accept(APPLICATION_JSON)
                                    .contentType(APPLICATION_JSON)
                                    .content(BaseResourceTest.asJsonString(artistDataTable))
                            )
                            .andDo(print())
                            .andExpect(status().isCreated())
                            .andReturn();
            artistId = UUID.fromString(JsonPath.read(result.getResponse().getContentAsString(), "$.result[0]"));

            assertThat(artistId)
                    .isNotNull()
                    .isInstanceOf(UUID.class);
        });
        Then("I should see that the current identifier has the following data",
                (DataTable dataTable) -> {

            Map<String, String> artistAsMap = dataTable.asMaps().get(0);

            ArtistDTO dbArtistDTO = jdbcTemplate.queryForObject(
                String.format("SELECT * FROM t_artist WHERE c_id = '%s'", artistId.toString()),
                new ArtistDTORowMapper()
            );

            assertThat(dbArtistDTO)
                    .isNotNull()
                    .returns(artistAsMap.get("name"), ArtistDTO::getName)
                    .returns(artistAsMap.get("phone"), ArtistDTO::getPhone)
                    .returns(artistAsMap.get("city"), ArtistDTO::getCity);

        });
        And("Following data are in database",
                (DataTable dataTable) -> {
            assertThat(
                    dataTable
                            .asMaps()
                            .stream()
                            .allMatch(this::isPresentInTheDatabase)
            ).isTrue();
});
        Then("I should see that attempt to create a new artist with the following data will fail with status error {string} and error code {string}",
                (String httpStatus, String errorCode, DataTable dataTable) -> {

            ArtistDTO artistDataTable = getArtistFromFeature(dataTable);

            MvcResult result = mvc.perform(post(API_ARTIST_BASE_ROUTE)
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content(BaseResourceTest.asJsonString(artistDataTable))
                    )
                    .andDo(print())
                    .andReturn();

            assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
            assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });
        When("I fetch artist with id {string}",
                (String id) -> {
                    MvcResult result = mvc.perform(get(API_ARTIST_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();

                    Gson gson = new Gson();
                    artistDTO = parseJsonToObject(result);

                    assertThat(artistDTO)
                            .isNotNull()
                            .isInstanceOf(ArtistDTO.class);

        });
        Then("I should the following artist is returned from database",
                (DataTable dataTable) -> {

            ArtistDTO artistDataTable = getArtistFromFeature(dataTable);

            assertThat(artistDTO)
                    .isNotNull()
                    .returns(artistDataTable.getId(), ArtistDTO::getId)
                    .returns(artistDataTable.getName(), ArtistDTO::getName)
                    .returns(artistDataTable.getPhone(), ArtistDTO::getPhone)
                    .returns(artistDataTable.getCity(), ArtistDTO::getCity);

        });
        Then("The attempt to fetch an artist with the id {string} will fail with status error {string} and error code {string}",
                (String id,  String httpStatus, String errorCode) -> {

                    MvcResult result = mvc.perform(get(API_ARTIST_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isNotFound())
                            .andReturn();
                    assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                    assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);
        });
        When("I fetch artist with name {string}", (String artistName) -> {
            MvcResult result = mvc.perform(get(API_ARTIST_BASE_ROUTE + "/name/" + artistName)
                            .accept(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            artistDTOS = parseJsonToList(result);

            assertThat(artistDTOS)
                    .hasSizeGreaterThan(0)
                    .isInstanceOf(ArrayList.class);
        });

        Then("The attempt to fetch an artist with the name {string} will return empty list",
                (String artistName) -> {
                MvcResult result = mvc.perform(get(API_ARTIST_BASE_ROUTE + "/name/" + artistName)
                                .accept(APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

                artistDTOS = parseJsonToList(result);

                assertThat(artistDTOS)
                        .hasSize(0)
                        .isInstanceOf(ArrayList.class);
        });
        Then("The attempt to fetch all artist from database will return a list of artist",
                () -> {
                MvcResult result = mvc.perform(get(API_ARTIST_BASE_ROUTE)
                                .accept(APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

                artistDTOS = parseJsonToList(result);

                assertThat(artistDTOS)
                        .hasSizeGreaterThan(0)
                        .isInstanceOf(ArrayList.class);
        });
        When("I delete artist with id {string}",
                (String id) -> {
                MvcResult result = mvc.perform(delete(API_ARTIST_BASE_ROUTE + "/" + id)
                                .accept(APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();


                msg = JsonPath.read(result.getResponse().getContentAsString(), "$.message");


        });

        Then("The attempt to delete an artist with the id {string} will fail with status error {string} and error code {string}",
                (String id, String httpStatus, String errorCode) -> {

                MvcResult result = mvc.perform(delete(API_ARTIST_BASE_ROUTE + "/" + id)
                                .accept(APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn();
                assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });

        Then("I should the see that the artist with id {string} is no longer in database and success message is {string}",
                (String id, String message) -> {

                    assertThat(msg).isEqualTo(message);

                    MvcResult result = mvc.perform(get(API_ARTIST_BASE_ROUTE + "/" + id)
                                    .accept(APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isNotFound())
                            .andReturn();
        });
        And("The following artist is store in database",
                (DataTable dataTable) -> {

                String id = dataTable.asMaps().get(0).get("id");

                Map<String, String> artistAsMap = dataTable.asMaps().get(0);

                ArtistDTO dbArtistDTO = jdbcTemplate.queryForObject(
                        String.format("SELECT * FROM t_artist WHERE c_id = '%s'", id),
                        new ArtistDTORowMapper()
                );

                assertThat(dbArtistDTO)
                        .isNotNull()
                        .returns(UUID.fromString(artistAsMap.get("id")), ArtistDTO::getId)
                        .returns(artistAsMap.get("name"), ArtistDTO::getName)
                        .returns(artistAsMap.get("phone"), ArtistDTO::getPhone)
                        .returns(artistAsMap.get("city"), ArtistDTO::getCity);
        });
        When("I update artist with id {string} with the following data",
                (String id, DataTable dataTable) -> {

                ArtistDTO artistDataTable = getArtistFromFeature(dataTable);

                MvcResult result = mvc.perform(put(API_ARTIST_BASE_ROUTE + "/" + id)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(BaseResourceTest.asJsonString(artistDataTable))
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
                artistDTO = parseJsonToObject(result);

                assertThat(artistDTO)
                        .isNotNull()
                        .isInstanceOf(ArtistDTO.class);
        });
        Then("I should the following artist is saved to database",
                (DataTable dataTable) -> {

            Map<String, String> artistAsMap = dataTable.asMaps().get(0);

            assertThat(artistDTO)
                    .isNotNull()
                    .returns(UUID.fromString(artistAsMap.get("id")), ArtistDTO::getId)
                    .returns(artistAsMap.get("name"), ArtistDTO::getName)
                    .returns(artistAsMap.get("phone"), ArtistDTO::getPhone)
                    .returns(artistAsMap.get("city"), ArtistDTO::getCity);
        });

        Then("The attempt to update an artist with the id {string} and following data will fail with status error {string} and error code {string}",
                (String id, String httpStatus, String errorCode, DataTable dataTable) -> {

                ArtistDTO artistDataTable = getArtistFromFeature(dataTable);

                MvcResult result = mvc.perform(put(API_ARTIST_BASE_ROUTE + "/" + id)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(BaseResourceTest.asJsonString(artistDataTable))
                        )
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn();
                assertThat(result.getResponse().getStatus()).isEqualTo(Integer.valueOf(errorCode));
                assertThat(HttpStatus.valueOf(result.getResponse().getStatus()).name()).isEqualTo(httpStatus);

        });

    }

    private ArtistDTO parseJsonToObject(MvcResult result) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        return gson.fromJson(String.valueOf((LinkedHashMap)
                JsonPath.read(result.getResponse().getContentAsString(), "$.result[0]")), ArtistDTO.class);
    }

    private List<ArtistDTO> parseJsonToList(MvcResult result) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        Type artistDTOListType = new TypeToken<ArrayList<ArtistDTO>>(){}.getType();
        return gson.fromJson(String.valueOf((ArrayList<LinkedHashMap>)
                JsonPath.read(result.getResponse().getContentAsString(), "$.result[0]")), artistDTOListType);
    }

    private boolean isPresentInTheDatabase(Map<String, String> data) {
        assertThat(
                jdbcTemplate.queryForList(
                        String.format(
                                "SELECT * FROM t_artist WHERE c_id = '%s'",
                                data.get("id")
                        )
                )
        ).isNotEmpty();
        return true;
    }

    private ArtistDTO getArtistFromFeature(DataTable dataTable) {
        if (dataTable.asMaps().get(0).get("id") != null) {
            return artistDTO = new ArtistDTO()
                    .id(UUID.fromString(dataTable.asMaps().get(0).get("id")))
                    .name(dataTable.asMaps().get(0).get("name"))
                    .phone(dataTable.asMaps().get(0).get("phone"))
                    .city(dataTable.asMaps().get(0).get("city"))
                    .facebookLink(dataTable.asMaps().get(0).get("facebookLink"))
                    .imageLink(dataTable.asMaps().get(0).get("imageLink"))
                    .websiteLink(dataTable.asMaps().get(0).get("websiteLink"))
                    .seekingVenue(Boolean.valueOf(dataTable.asMaps().get(0).get("seekingVenue")))
                    .seekingDescription(dataTable.asMaps().get(0).get("seekingDescription"));
        } else {
            return artistDTO = new ArtistDTO()
                    .name(dataTable.asMaps().get(0).get("name"))
                    .phone(dataTable.asMaps().get(0).get("phone"))
                    .city(dataTable.asMaps().get(0).get("city"))
                    .facebookLink(dataTable.asMaps().get(0).get("facebookLink"))
                    .imageLink(dataTable.asMaps().get(0).get("imageLink"))
                    .websiteLink(dataTable.asMaps().get(0).get("websiteLink"))
                    .seekingVenue(Boolean.valueOf(dataTable.asMaps().get(0).get("seekingVenue")))
                    .seekingDescription(dataTable.asMaps().get(0).get("seekingDescription"));

        }
    }
}
