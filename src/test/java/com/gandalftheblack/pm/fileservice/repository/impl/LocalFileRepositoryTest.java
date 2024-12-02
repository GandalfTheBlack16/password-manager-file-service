package com.gandalftheblack.pm.fileservice.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocalFileRepositoryTest {
  @InjectMocks private LocalFileRepository localFileRepository;

  @Test
  void shouldReturnFilePath() {
    String path = System.getProperty("java.io.tmpdir") + "/" + "testfile.txt";
    String filePath = localFileRepository.saveFile(new File(path));
    assertEquals(path, filePath);
  }
}
