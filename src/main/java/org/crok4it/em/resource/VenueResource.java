package org.crok4it.em.resource;

import lombok.RequiredArgsConstructor;
import org.crok4it.em.api.VenueApi;
import org.crok4it.em.dto.SuccessResponse;
import org.crok4it.em.dto.VenueDTO;
import org.crok4it.em.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

import static org.crok4it.em.constant.CommonConstant.API_BASE_ROUTE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_BASE_ROUTE)
public class VenueResource implements VenueApi {

    private final VenueService venueService;

    @Override
    public ResponseEntity<SuccessResponse> createVenue(VenueDTO venueDTO) {
        UUID venueId = null;
        try {
            venueId = venueService.createVenue(venueDTO);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(CREATED).body(
               new SuccessResponse()
                       .success(true)
                       .message("Venue created successfully")
                       .result(Collections.singletonList(venueId))
        );
    }

    @Override
    public ResponseEntity<SuccessResponse> findVenueById(UUID id) {
        VenueDTO venueDTO = venueService.findById(id.toString());
        return ResponseEntity.status(OK).body(
                new SuccessResponse()
                        .message("Venue fetch successfully")
                        .success(true)
                        .result(Collections.singletonList(venueDTO))
        );
    }
}
