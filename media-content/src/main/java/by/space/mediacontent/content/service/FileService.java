package by.space.mediacontent.content.service;

import by.space.mediacontent.content.dto.VenueDocumentDto;
import by.space.mediacontent.content.dto.VenueDocumentStream;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    void uploadDocuments(Long venueId, List<MultipartFile> files, List<Long> venueAddressIds);

    /**
     * Gets all documents for selected venue address
     *
     * @param venueId        venue id of selected venue address
     * @param venueAddressId venue address
     * @return list of all venue address documents
     */
    List<VenueDocumentDto> listVenueDocuments(Long venueId, Long venueAddressId);

    /**
     * Download documents from minio
     *
     * @param documentId document which will be downloaded from minio
     * @return document
     */
    VenueDocumentStream downloadDocument(Long documentId);
}
