package com.gandalftheblack.pm.fileservice.handler;

import com.gandalftheblack.pm.fileservice.model.exception.EmptyMultipartFileException;
import com.gandalftheblack.pm.fileservice.model.exception.FileUploadException;
import com.gandalftheblack.pm.fileservice.model.exception.InvalidTokenException;
import com.gandalftheblack.pm.fileservice.model.exception.UnauthenticatedUserException;
import com.gandalftheblack.pm.fileservice.model.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidTokenException.class)
  protected ResponseEntity<Object> handleInvalidTokenException(
      RuntimeException ex, WebRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse("Bad Request", "Token provided could not be verified");
    return handleExceptionInternal(
        ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(UnauthenticatedUserException.class)
  protected ResponseEntity<Object> handleUnauthenticatedUserException(
      RuntimeException ex, WebRequest request) {
    UnauthenticatedUserException exception = (UnauthenticatedUserException) ex;
    ErrorResponse errorResponse =
        new ErrorResponse(
            "Unauthorized", String.format("User authentication failed: %s", exception.getEmail()));
    return handleExceptionInternal(
        ex, errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler(FileUploadException.class)
  protected ResponseEntity<Object> handleFileUploadException(
      RuntimeException ex, WebRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse("Internal error", "Unexpected error uploading file");
    return handleExceptionInternal(
        ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler({MultipartException.class, EmptyMultipartFileException.class})
  protected ResponseEntity<Object> handleMultipartFilesException(
      RuntimeException ex, WebRequest request) {
    ErrorResponse errorResponse =
        new ErrorResponse("Bad request", "Files multipart param must not be empty");
    return handleExceptionInternal(
        ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
}
