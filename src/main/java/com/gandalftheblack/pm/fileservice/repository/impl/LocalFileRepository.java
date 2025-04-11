package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.model.exception.FileDownloadException;
import com.gandalftheblack.pm.fileservice.model.exception.FileUploadException;
import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@Profile("local")
public class LocalFileRepository implements FileRepository {
  @Override
  public String saveFile(MultipartFile file) {
    return transformMultipartFile(file).getAbsolutePath();
  }

  @Override
  public File getFile(String fileId, String fileName) {
    File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
    if (!file.exists()) {
      throw new FileDownloadException(new RuntimeException("File not found"));
    }
    return file;
  }

  private File transformMultipartFile(MultipartFile file) {
    File uploadedFile =
            new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
    try {
      file.transferTo(uploadedFile);
    } catch (IOException e) {
      throw new FileUploadException(e);
    }
    return uploadedFile;
  }
}
