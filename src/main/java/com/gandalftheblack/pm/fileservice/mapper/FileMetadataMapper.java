package com.gandalftheblack.pm.fileservice.mapper;

import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface FileMetadataMapper {
    @Mapping(source = "id", target = "fileId")
    FilePostResponse entityToPostResponse(FileMetadataEntity entity);
    FileGetResponse entityToGetResponse(FileMetadataEntity optionalEntity);
    default Optional<FileGetResponse> entityToGetResponse(Optional<FileMetadataEntity> optionalEntity) {
        return optionalEntity.map(this::entityToGetResponse);
    }
}
