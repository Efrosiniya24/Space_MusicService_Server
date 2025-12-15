package by.space.users_service.feign;

import by.space.users_service.model.dto.ImageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "MEDIA-CONTENT")
public interface MediaClient {

    @PostMapping("/addImage")
    ImageDto addImage(@RequestParam final MultipartFile file, @RequestParam final Long ownerId);
}
