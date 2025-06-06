package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.client.GoogleDriveClient;
import com.gandalftheblack.pm.fileservice.model.exception.FileDownloadException;
import com.gandalftheblack.pm.fileservice.model.exception.FileUploadException;
import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@RequiredArgsConstructor
@Profile("pro")
public class GoogleDriveRepository implements FileRepository {

  @Value("${google.drive.target_folder_id}")
  private String folderId;

  private final GoogleDriveClient googleDriveClient;

  @Override
  public String saveFile(MultipartFile file) {
    try {
      Drive drive = googleDriveClient.getInstance();
      File fileMetadata = new File();
      fileMetadata.setParents(Collections.singletonList(folderId));
      fileMetadata.setName(file.getOriginalFilename());
      File remoteFile =
          drive
              .files()
              .create(
                  fileMetadata,
                  new InputStreamContent(
                      file.getContentType(), new ByteArrayInputStream(file.getBytes())))
              .execute();
      return remoteFile.getId();
    } catch (IOException e) {
      throw new FileUploadException(e);
    }
  }

  @Override
  public java.io.File getFile(String fileId, String fileName) {
    try {
      Drive drive = googleDriveClient.getInstance();
      java.io.File file = new java.io.File(fileName);
      try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
        drive.files().get(fileId).executeMediaAndDownloadTo(fileOutputStream);
        return file;
      }
    } catch (IOException e) {
      throw new FileDownloadException(e);
    }
  }
}
