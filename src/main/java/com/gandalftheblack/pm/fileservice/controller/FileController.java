package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.model.dto.FileDto;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/api/file")
    public ResponseEntity<FilePostResponse> postFile(@RequestBody FileDto fileDto) {
        FilePostResponse response = fileService.createFile(fileDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
