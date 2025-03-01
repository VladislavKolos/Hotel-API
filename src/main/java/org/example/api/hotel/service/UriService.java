package org.example.api.hotel.service;

import org.example.api.hotel.service.enums.UriType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class UriService {
    public String createUri(UriType type, UUID id) {
        return UriComponentsBuilder.fromPath(type.getPath())
                .buildAndExpand(id)
                .toUriString();
    }
}