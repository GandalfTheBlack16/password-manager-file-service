package com.gandalftheblack.pm.fileservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ErrorResponse extends HttpBaseResponse{
    private String status;
    private String message;
}
