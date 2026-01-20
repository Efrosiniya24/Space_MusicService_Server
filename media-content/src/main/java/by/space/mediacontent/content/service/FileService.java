package by.space.mediacontent.content.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * method for uploading documents that confirm venue identity
     *
     * @param venueId venue which document uploads
     * @param files   files that confirm venue identity
     */
    void uploadDocuments(Long venueId, List<MultipartFile> files);
}
