package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.exception.InvalidTokenException;
import com.gandalftheblack.pm.fileservice.exception.UnauthenticatedUserException;
import com.gandalftheblack.pm.fileservice.model.response.ErrorResponse;
import com.gandalftheblack.pm.fileservice.model.response.HttpBaseResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import com.gandalftheblack.pm.fileservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final SecurityService securityService;

    @PostMapping("/api/file")
    public ResponseEntity<HttpBaseResponse> postFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam List<MultipartFile> files
            ){
        try {
            if (files.isEmpty()) {
                return new ResponseEntity<>(new ErrorResponse("Bad request", "File is empty"), HttpStatus.BAD_REQUEST);
            }
            String userId = securityService.getUserIdFromToken(authHeader);
            MultipleFilePostResponse response = fileService.uploadFile(files, userId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UnauthenticatedUserException e) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized", String.format("User authentication failed: %s", e.getEmail())),
                    HttpStatus.UNAUTHORIZED);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid token", "Token provided could not be verified"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Unexpected error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
