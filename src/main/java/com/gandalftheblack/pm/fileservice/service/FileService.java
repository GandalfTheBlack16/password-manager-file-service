package com.gandalftheblack.pm.fileservice.service;

import com.gandalftheblack.pm.fileservice.mapper.FileMetadataMapper;
import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FileDownloadResponse;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
  private final FileMetadataRepository fileMetadataRepository;
  private final FileRepository fileRepository;
  private final FileMetadataMapper fileMetadataMapper;

  public MultipleFilePostResponse uploadFile(List<MultipartFile> multipartFileList, String userId) {
    List<FilePostResponse> filePostResponses = new LinkedList<>();
    for (MultipartFile multipartFile : multipartFileList) {
      String fileId = fileRepository.saveFile(multipartFile);
      FileMetadataEntity fileMetadataEntity = buildFileMetadata(multipartFile, userId, fileId);
      filePostResponses.add(
          fileMetadataMapper.entityToPostResponse(fileMetadataRepository.save(fileMetadataEntity)));
    }
    return new MultipleFilePostResponse(filePostResponses);
  }

  public Page<FileGetResponse> getFilesOfUser(
      String userId, List<FileStatus> status, String query, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("creationDate")));
    return fileMetadataRepository.findAllByOwnerFilteredByStatusAndName(
        userId, status, query, pageable);
  }

  public FileDownloadResponse getFileById(String userId, String fileId) {
    FileMetadataEntity fileMetadataEntity = fileMetadataRepository.findByOwnerAndId(userId, fileId).orElse(null);
    if (fileMetadataEntity == null) {
        return null;
    }
    File file;
    if (Files.exists(Path.of(fileMetadataEntity.getFileName()))){
      file = new File(fileMetadataEntity.getFileName());
    }
    else {
      file = fileRepository.getFile(fileMetadataEntity.getObjectId(), fileMetadataEntity.getFileName());
    }
    return FileDownloadResponse.builder()
        .fileName(fileMetadataEntity.getFileName())
        .fileSize(fileMetadataEntity.getFileSize())
        .mimeType(fileMetadataEntity.getMimeType())
        .file(file)
        .build();
  }

  public boolean deleteFileById(String userId, String fileId) {
    Optional<FileMetadataEntity> optionalFileMetadataEntity =
        fileMetadataRepository.findByOwnerAndId(userId, fileId);
    if (optionalFileMetadataEntity.isEmpty()) {
      return false;
    }
    FileMetadataEntity fileMetadataEntity = optionalFileMetadataEntity.get();
    fileMetadataEntity.setStatus(FileStatus.DELETED);
    fileMetadataEntity.setDeletionDate(new Date());
    fileMetadataRepository.save(fileMetadataEntity);
    return true;
  }

  public Optional<FileGetResponse> restoreFile(String userId, String fileId) {
    Optional<FileMetadataEntity> optionalFileMetadataEntity =
        fileMetadataRepository.findByOwnerAndId(userId, fileId);
    if (optionalFileMetadataEntity.isPresent()) {
      FileMetadataEntity metadataEntity = optionalFileMetadataEntity.get();
      metadataEntity.setStatus(FileStatus.RESTORED);
      metadataEntity.setModificationDate(new Date());
      optionalFileMetadataEntity = Optional.of(fileMetadataRepository.save(metadataEntity));
    }
    return fileMetadataMapper.entityToGetResponse(optionalFileMetadataEntity);
  }

  private FileMetadataEntity buildFileMetadata(
      MultipartFile multipartFile, String userId, String fileId) {
    return FileMetadataEntity.builder()
        .fileName(multipartFile.getOriginalFilename())
        .owner(userId)
        .objectId(fileId)
        .mimeType(multipartFile.getContentType())
        .fileSize(multipartFile.getSize())
        .status(FileStatus.CREATED)
        .creationDate(new Date())
        .build();
  }
}
