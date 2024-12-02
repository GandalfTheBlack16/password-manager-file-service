package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.exception.EmptyMultipartFileException;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import com.gandalftheblack.pm.fileservice.service.SecurityService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
  public ResponseEntity<FileGetResponse> getFileById(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable String id) {
    String userId = securityService.getUserIdFromToken(authHeader);
    Optional<FileGetResponse> optionalResponse = fileService.getFileById(userId, id);
    return optionalResponse
        .map(ResponseEntity::ok)
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }
}
