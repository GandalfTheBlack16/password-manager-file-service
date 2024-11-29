package com.gandalftheblack.pm.fileservice.service;

import com.gandalftheblack.pm.fileservice.mapper.FileMetadataMapper;
import com.gandalftheblack.pm.fileservice.model.dto.FileDto;
import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileMetadataMapper fileMetadataMapper;

    public FilePostResponse createFile(FileDto fileDto) {
        FileMetadataEntity entity = fileMetadataMapper.dtoToEntity(fileDto);
        entity.setCreationDate(Date.from(Instant.now()));
        entity.setStatus(FileStatus.CREATED);
        return fileMetadataMapper.entityToResponse(fileMetadataRepository.save(entity));
    }
}
