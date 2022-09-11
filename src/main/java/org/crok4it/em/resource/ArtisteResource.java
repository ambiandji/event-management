package org.crok4it.em.resource;


import lombok.RequiredArgsConstructor;
import org.crok4it.em.api.ArtistApi;
import org.crok4it.em.dto.ArtistDTO;
import org.crok4it.em.dto.SuccessResponse;
import org.crok4it.em.service.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

import static org.crok4it.em.constant.CommonConstant.API_BASE_ROUTE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(API_BASE_ROUTE)
@RequiredArgsConstructor
public class ArtisteResource implements ArtistApi {

    private final ArtistService artistService;

    @Override
    public ResponseEntity<SuccessResponse> createArtist(ArtistDTO artistDTO) {
        UUID artistId = null;
        try {
            artistId = artistService.createArtist(artistDTO);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity
                    .status(CREATED)
                    .body(
                            new SuccessResponse()
                                .success(true)
                                .message("Artist created successfully")
                                .result(Collections.singletonList(artistId)));

    }
}
