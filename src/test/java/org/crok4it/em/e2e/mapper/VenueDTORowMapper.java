package org.crok4it.em.e2e.mapper;

import org.crok4it.em.dto.VenueDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class VenueDTORowMapper implements RowMapper<VenueDTO> {
    @Override
    public VenueDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new VenueDTO()
                .id(UUID.fromString(rs.getString("c_id")))
                .name(rs.getString("c_name"))
                .phone(rs.getString("c_phone"))
                .city(rs.getString("c_city"))
                .facebookLink(rs.getString("c_facebook_link"))
                .imageLink(rs.getString("c_image_link"))
                .websiteLink(rs.getString("c_website_link"))
                .seekingTalent(rs.getBoolean("c_seeking_talent"))
                .seekingDescription(rs.getString("c_seeking_description"));
    }
}
