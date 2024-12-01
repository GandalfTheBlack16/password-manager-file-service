package com.gandalftheblack.pm.fileservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UnauthenticatedUserException extends Exception {
    private String email;
}
