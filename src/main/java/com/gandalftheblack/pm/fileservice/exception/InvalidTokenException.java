package com.gandalftheblack.pm.fileservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidTokenException extends Exception{
}
