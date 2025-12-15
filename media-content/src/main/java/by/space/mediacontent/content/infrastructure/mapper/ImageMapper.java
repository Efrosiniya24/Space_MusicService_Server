package by.space.mediacontent.content.infrastructure.mapper;

import by.space.mediacontent.content.application.dto.ImageDto;
import by.space.mediacontent.content.domain.entity.ImageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDto toImageDto(ImageEntity imageEntity);

    ImageEntity toImageEntity(ImageDto imageDto);
}
