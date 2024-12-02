package com.gandalftheblack.pm.fileservice.service;

import com.gandalftheblack.pm.fileservice.mapper.FileMetadataMapper;
import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.exception.FileUploadException;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.repository.FileMetadataRepository;
import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileRepository fileRepository;
    private final FileMetadataMapper fileMetadataMapper;

    public MultipleFilePostResponse uploadFile(List<MultipartFile> multipartFileList, String userId) {
        List<FilePostResponse> filePostResponses = new LinkedList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            String path = fileRepository.saveFile(transformMultipartFile(multipartFile));
            FileMetadataEntity fileMetadataEntity = buildFileMetadata(multipartFile, userId, path);
            filePostResponses.add(fileMetadataMapper.entityToPostResponse(fileMetadataRepository.save(fileMetadataEntity)));
        }
        return new MultipleFilePostResponse(filePostResponses);
    }

    public Page<FileGetResponse> getFilesOfUser(String userId, List<FileStatus> status, String query, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("creationDate")));
        return fileMetadataRepository.findAllByOwnerFilteredByStatusAndName(userId, status, query, pageable);
    }

    private File transformMultipartFile (MultipartFile file) {
        File uploadedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try {
            file.transferTo(uploadedFile);
        } catch (IOException e) {
            throw new FileUploadException(e);
        }
        return uploadedFile;
    }

    private FileMetadataEntity buildFileMetadata (MultipartFile multipartFile, String userId, String path) {
        return FileMetadataEntity
                .builder()
                .fileName(multipartFile.getOriginalFilename())
                .owner(userId)
                .mimeType(multipartFile.getContentType())
                .fileSize(multipartFile.getSize())
                .path(path)
                .status(FileStatus.CREATED)
                .creationDate(Date.from(Instant.now()))
                .build();
    }
}
