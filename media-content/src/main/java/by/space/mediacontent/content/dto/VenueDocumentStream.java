package by.space.mediacontent.content.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Getter
@RequiredArgsConstructor
public class VenueDocumentStream {
    private final Resource resource;
    private final MediaType mediaType;
    private final String filename;
}
