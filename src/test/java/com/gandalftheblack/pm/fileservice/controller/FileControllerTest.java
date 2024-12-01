package com.gandalftheblack.pm.fileservice.controller;

import com.gandalftheblack.pm.fileservice.exception.InvalidTokenException;
import com.gandalftheblack.pm.fileservice.exception.UnauthenticatedUserException;
import com.gandalftheblack.pm.fileservice.model.response.ErrorResponse;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.model.response.HttpBaseResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.service.FileService;
import com.gandalftheblack.pm.fileservice.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {
    @Mock
    private FileService fileService;
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private FileController fileController;

    @Test
    void shouldReturnErrorResponseIfNotFilesSelectedToUpload() {
        ResponseEntity<HttpBaseResponse> response = fileController.postFile("mock", List.of());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("File is empty", errorResponse.getMessage());
    }

    @Test
    void shouldReturnUnauthorizedResponseIfUserAuthenticationFails() throws InvalidTokenException, UnauthenticatedUserException {
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new byte[]{});
        when(securityService.getUserIdFromToken(anyString())).thenThrow(UnauthenticatedUserException.class);
        ResponseEntity<HttpBaseResponse> response = fileController.postFile("mock", List.of(multipartFile));
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        assertTrue(errorResponse.getMessage().contains("User authentication failed"));
    }

    @Test
    void shouldReturnBadRequestResponseIfTokenIsInvalid() throws InvalidTokenException, UnauthenticatedUserException {
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new byte[]{});
        when(securityService.getUserIdFromToken(anyString())).thenThrow(InvalidTokenException.class);
        ResponseEntity<HttpBaseResponse> response = fileController.postFile("mock", List.of(multipartFile));
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("Token provided could not be verified", errorResponse.getMessage());
    }

    @Test
    void shouldReturnInternalServerErrorResponse() throws InvalidTokenException, UnauthenticatedUserException, IOException {
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new byte[]{});
        when(securityService.getUserIdFromToken(anyString())).thenReturn("1111");
        when(fileService.uploadFile(List.of(multipartFile), "1111")).thenThrow(IOException.class);
        ResponseEntity<HttpBaseResponse> response = fileController.postFile("mock", List.of(multipartFile));
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Unexpected error", errorResponse.getStatus());
    }

    @Test
    void shouldReturnMultipleFileUploadResponse() throws InvalidTokenException, UnauthenticatedUserException, IOException {
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new byte[]{});
        MultipleFilePostResponse expectedResponse = new MultipleFilePostResponse(List.of(
                FilePostResponse
                        .builder()
                        .fileName("test.txt")
                        .status("CREATED")
                        .mimeType("text/plain")
                        .build()
        ));
        when(securityService.getUserIdFromToken(anyString())).thenReturn("1111");
        when(fileService.uploadFile(List.of(multipartFile), "1111")).thenReturn(expectedResponse);
        ResponseEntity<HttpBaseResponse> response = fileController.postFile("mock", List.of(multipartFile));
        MultipleFilePostResponse fileUploadResponse = (MultipleFilePostResponse) response.getBody();
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertEquals(expectedResponse, fileUploadResponse);

    }}