package org.example.api.hotel.util.uri;

import lombok.experimental.UtilityClass;
import org.example.api.hotel.util.uri.enums.UriType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@UtilityClass
public class UriUtil {
    public static String createUri(UriType type, UUID id) {
        return UriComponentsBuilder.fromPath(type.getPath())
                .buildAndExpand(id)
                .toUriString();
    }
}