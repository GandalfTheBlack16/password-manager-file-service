package com.gandalftheblack.pm.fileservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.gandalftheblack.pm.fileservice.mapper.FileMetadataMapper;
import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.repository.FileMetadataRepository;
import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
  @Mock private FileMetadataRepository fileMetadataRepository;
  @Mock private FileRepository fileRepository;
  @Mock private FileMetadataMapper fileMetadataMapper;
  @InjectMocks private FileService fileService;

  @Test
  void shouldUploadSingleFile() {
    MultipartFile multipartFile =
        new MockMultipartFile("test.txt", "test.txt", "text/plain", new byte[] {});
    when(fileRepository.saveFile(any())).thenReturn("/dir/text.txt");
    FileMetadataEntity expectedFileMetadata =
        FileMetadataEntity.builder()
            .fileName("test.txt")
            .owner("1111")
            .mimeType("text/plain")
            .path("/dir/text.txt")
            .status(FileStatus.CREATED)
            .fileSize(multipartFile.getSize())
            .build();
    when(fileMetadataRepository.save(any())).thenReturn(expectedFileMetadata);
    when(fileMetadataMapper.entityToPostResponse(any()))
        .thenReturn(
            FilePostResponse.builder()
                .status("CREATED")
                .fileName("test.txt")
                .mimeType("text/plain")
                .build());
    assertDoesNotThrow(
        () -> {
          MultipleFilePostResponse response =
              fileService.uploadFile(List.of(multipartFile), "1111");
          verify(fileRepository, times(1)).saveFile(any());
          verify(fileMetadataRepository, times(1)).save(any());
          assertEquals(1, response.getUploadedFiles().size());
          assertEquals("test.txt", response.getUploadedFiles().getFirst().getFileName());
          assertEquals("CREATED", response.getUploadedFiles().getFirst().getStatus());
          assertEquals("text/plain", response.getUploadedFiles().getFirst().getMimeType());
        });
  }

  @Test
  void shouldGetFilesForSpecificUser() {
    when(fileMetadataRepository.findAllByOwnerFilteredByStatusAndName(
            eq("1"), anyList(), eq("filter"), any()))
        .thenReturn(
            new PageImpl<>(
                List.of(
                    FileGetResponse.builder()
                        .id("0001")
                        .status(FileStatus.CREATED)
                        .fileName("filterFile.png")
                        .build())));
    Page<FileGetResponse> response =
        fileService.getFilesOfUser("1", List.of(FileStatus.CREATED), "filter", 0, 5);
    verify(fileMetadataRepository, times(1))
        .findAllByOwnerFilteredByStatusAndName(anyString(), anyList(), anyString(), any());
    assertEquals(1, response.getTotalElements());
    assertTrue(response.get().findFirst().isPresent());
    assertEquals("0001", response.get().findFirst().get().getId());
    assertEquals(FileStatus.CREATED, response.get().findFirst().get().getStatus());
    assertEquals("filterFile.png", response.get().findFirst().get().getFileName());
  }

  @Test
  void shouldGetFileById() {
    FileMetadataEntity metadataEntity =
        FileMetadataEntity.builder()
            .id("0001")
            .owner("1")
            .fileName("demo.txt")
            .mimeType("text/plain")
            .build();
    when(fileMetadataRepository.findByOwnerAndId("1", "0001"))
        .thenReturn(Optional.of(metadataEntity));
    when(fileMetadataMapper.entityToGetResponse(Optional.of(metadataEntity)))
        .thenReturn(
            Optional.of(
                FileGetResponse.builder()
                    .id("0001")
                    .fileName("demo.txt")
                    .mimeType("text/plain")
                    .build()));
    Optional<FileGetResponse> response = fileService.getFileById("1", "0001");
    assertTrue(response.isPresent());
    assertEquals("0001", response.get().getId());
    assertEquals("demo.txt", response.get().getFileName());
    assertEquals("text/plain", response.get().getMimeType());
  }

  @Test
  void shouldDeleteFileById() {
    FileMetadataEntity metadataEntity =
        FileMetadataEntity.builder()
            .id("0001")
            .owner("1")
            .fileName("demo.txt")
            .status(FileStatus.CREATED)
            .mimeType("text/plain")
            .build();
    when(fileMetadataRepository.findByOwnerAndId("1", "0001"))
        .thenReturn(Optional.of(metadataEntity));

    boolean result = fileService.deleteFileById("1", "0001");
    verify(fileMetadataRepository, times(1)).save(any(FileMetadataEntity.class));
    assertTrue(result);
  }

  @Test
  void shouldNotDeleteNoExistingFileById() {
    when(fileMetadataRepository.findByOwnerAndId("1", "0001")).thenReturn(Optional.empty());
    boolean result = fileService.deleteFileById("1", "0001");
    verify(fileMetadataRepository, times(0)).save(any(FileMetadataEntity.class));
    assertFalse(result);
  }

  @Test
  void shouldRestoreFileById() {
    FileMetadataEntity metadataEntity =
        FileMetadataEntity.builder()
            .id("0001")
            .owner("1")
            .fileName("demo.txt")
            .status(FileStatus.CREATED)
            .mimeType("text/plain")
            .build();
    when(fileMetadataRepository.findByOwnerAndId("1", "0001"))
        .thenReturn(Optional.of(metadataEntity));
    when(fileMetadataMapper.entityToGetResponse(Optional.of(metadataEntity)))
        .thenReturn(
            Optional.of(
                FileGetResponse.builder()
                    .id("0001")
                    .fileName("demo.txt")
                    .mimeType("text/plain")
                    .build()));
    when(fileMetadataRepository.save(any())).thenReturn(metadataEntity);
    Optional<FileGetResponse> response = fileService.restoreFile("1", "0001");
    assertTrue(response.isPresent());
    assertEquals("0001", response.get().getId());
    assertEquals("demo.txt", response.get().getFileName());
    assertEquals("text/plain", response.get().getMimeType());
  }
}
