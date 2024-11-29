package com.gandalftheblack.pm.fileservice.mapper;

import com.gandalftheblack.pm.fileservice.model.dto.FileDto;
import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMetadataMapper {
    FileDto entityToDto(FileMetadataEntity entity);
    @Mapping(source = "id", target = "fileId")
    FilePostResponse entityToResponse(FileMetadataEntity entity);
    FileMetadataEntity dtoToEntity(FileDto dto);
}
