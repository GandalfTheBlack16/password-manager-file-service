package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import java.io.File;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("local")
public class LocalFileRepository implements FileRepository {
  @Override
  public String saveFile(File file) {
    return file.getAbsolutePath();
  }
}
