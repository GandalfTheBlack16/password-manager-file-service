package com.gandalftheblack.pm.fileservice.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class FileDownloadResponse {
    private String fileName;
    private String mimeType;
    private Long fileSize;
    private File file;
}
