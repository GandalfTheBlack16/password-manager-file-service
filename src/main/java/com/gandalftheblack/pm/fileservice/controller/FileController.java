package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.exception.InvalidTokenException;
import com.gandalftheblack.pm.fileservice.exception.UnauthenticatedUserException;
import com.gandalftheblack.pm.fileservice.model.dto.FileDto;
import com.gandalftheblack.pm.fileservice.model.response.ErrorResponse;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.model.response.HttpBaseResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import com.gandalftheblack.pm.fileservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final SecurityService securityService;

    @PostMapping("/api/file")
    public ResponseEntity<HttpBaseResponse> postFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody FileDto fileDto
    ){
        try {
            String userId = securityService.getUserIdFromToken(authHeader);
            FilePostResponse response = fileService.createFile(fileDto, userId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UnauthenticatedUserException e) {
            return new ResponseEntity<>(new ErrorResponse("Unauthorized", String.format("User authentication failed: %s", e.getEmail())),
                    HttpStatus.UNAUTHORIZED);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid token", "Token provided could not be verified"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
