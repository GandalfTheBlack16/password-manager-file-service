package com.gandalftheblack.pm.fileservice.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UnauthenticatedUserException extends RuntimeException {
  private String email;
}
