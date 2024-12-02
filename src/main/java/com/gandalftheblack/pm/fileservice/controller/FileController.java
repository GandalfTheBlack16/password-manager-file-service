package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FileListGetResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import com.gandalftheblack.pm.fileservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;
    private final SecurityService securityService;

    @PostMapping
    public ResponseEntity<MultipleFilePostResponse> postFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam List<MultipartFile> files
            ){
        String userId = securityService.getUserIdFromToken(authHeader);
        if (files.isEmpty() || files.getFirst().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Files multipart param must not be empty");
        }
        MultipleFilePostResponse response = fileService.uploadFile(files, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<FileListGetResponse> getFiles(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam(defaultValue = "CREATED") List<FileStatus> status
    ){
        String userId = securityService.getUserIdFromToken(authHeader);
        FileListGetResponse response = fileService.getFilesOfUser(userId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
