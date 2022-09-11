package org.crok4it.em.e2e.mapper;

import org.crok4it.em.dto.ArtistDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ArtistDTORowMapper implements RowMapper<ArtistDTO> {
    @Override
    public ArtistDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ArtistDTO()
                .id(UUID.fromString(rs.getString("c_id")))
                .name(rs.getString("c_name"))
                .phone(rs.getString("c_phone"))
                .city(rs.getString("c_city"))
                .facebookLink(rs.getString("c_facebook_link"))
                .imageLink(rs.getString("c_image_link"))
                .websiteLink(rs.getString("c_website_link"))
                .seekingVenue(rs.getBoolean("c_seeking_venue"))
                .seekingDescription(rs.getString("c_seeking_description"));
    }
}
