package com.gandalftheblack.pm.fileservice.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileRepository {
  String saveFile(MultipartFile file);
  File getFile(String fileId, String fileName);
}
