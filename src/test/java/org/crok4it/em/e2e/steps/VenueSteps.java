package org.crok4it.em.e2e.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.e2e.mapper.VenueDTORowMapper;
import org.crok4it.em.unit.resource.BaseResourceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.crok4it.em.constant.VenueConstant.API_VENUE_BASE_ROUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

                    VenueDTO venueDataTable = getArtistFromFeature(dataTable);

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
                    VenueDTO venueDataTable = getArtistFromFeature(dataTable);

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

    private VenueDTO getArtistFromFeature(DataTable dataTable) {
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