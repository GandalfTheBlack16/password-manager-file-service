package com.gandalftheblack.pm.fileservice.repository;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
  String saveFile(MultipartFile file);

  File getFile(String fileId, String fileName);
}
