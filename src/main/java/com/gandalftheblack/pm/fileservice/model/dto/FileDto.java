package com.gandalftheblack.pm.fileservice.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {
    private String fileName;
}
