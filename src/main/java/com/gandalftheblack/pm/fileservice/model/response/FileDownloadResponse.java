package com.gandalftheblack.pm.fileservice.model.response;

import java.io.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDownloadResponse {
  private String fileName;
  private String mimeType;
  private Long fileSize;
  private File file;
}
