package by.space.mediacontent.content.infrastructure.mapper;

import by.space.mediacontent.content.domain.entity.ImageEntity;
import by.space.mediacontent.content.dto.ImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDto toImageDto(ImageEntity imageEntity);

    ImageEntity toImageEntity(ImageDto imageDto);
}
