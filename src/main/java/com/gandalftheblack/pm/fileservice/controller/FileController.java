package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.exception.EmptyMultipartFileException;
import com.gandalftheblack.pm.fileservice.model.response.FileDownloadResponse;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import com.gandalftheblack.pm.fileservice.service.SecurityService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
  private final FileService fileService;
  private final SecurityService securityService;

  @PostMapping
  public ResponseEntity<MultipleFilePostResponse> postFile(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
      @RequestParam List<MultipartFile> files) {
    String userId = securityService.getUserIdFromToken(authHeader);
    if (files.isEmpty() || files.getFirst().isEmpty()) {
      throw new EmptyMultipartFileException();
    }
    MultipleFilePostResponse response = fileService.uploadFile(files, userId);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<FileGetResponse>> getFiles(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
      @RequestParam(defaultValue = "CREATED") List<FileStatus> status,
      @RequestParam(required = false) String query,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {
    String userId = securityService.getUserIdFromToken(authHeader);
    return ResponseEntity.ok(fileService.getFilesOfUser(userId, status, query, page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FileSystemResource> getFileById(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String id) {
    String userId = securityService.getUserIdFromToken(authHeader);
    FileDownloadResponse response = fileService.getFileById(userId, id);
    if (response == null) {
      return ResponseEntity.noContent().build();
    }
    FileSystemResource resource = new FileSystemResource(response.getFile());
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + response.getFileName());
    headers.add(HttpHeaders.CONTENT_TYPE, response.getMimeType());
    headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(response.getFileSize()));
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFile(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String id) {
    String userId = securityService.getUserIdFromToken(authHeader);
    boolean deleted = fileService.deleteFileById(userId, id);
    if (!deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/restore/{id}")
  public ResponseEntity<FileGetResponse> restoreFile(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String id) {
    String userId = securityService.getUserIdFromToken(authHeader);
    Optional<FileGetResponse> optionalResponse = fileService.restoreFile(userId, id);
    return optionalResponse
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
