package org.crok4it.em.e2e.steps;

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

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.crok4it.em.constant.ArtistConstant.API_ARTIST_BASE_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArtistSteps implements En{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mvc;

    private UUID artistId;

    private ArtistDTO artistDTO;

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
        Then("I should see that attempt to create a new artist will fail with status error {string} and error code {string}",
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
