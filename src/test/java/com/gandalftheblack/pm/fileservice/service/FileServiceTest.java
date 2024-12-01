package com.gandalftheblack.pm.fileservice.service;

import com.gandalftheblack.pm.fileservice.mapper.FileMetadataMapper;
import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FilePostResponse;
import com.gandalftheblack.pm.fileservice.model.response.MultipleFilePostResponse;
import com.gandalftheblack.pm.fileservice.repository.FileMetadataRepository;
import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    private FileMetadataRepository fileMetadataRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileMetadataMapper fileMetadataMapper;
    @InjectMocks
    private FileService fileService;

    @Test
    void shouldUploadSingleFile() {
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", new byte[]{});
        when(fileRepository.saveFile(any())).thenReturn("/dir/text.txt");
        FileMetadataEntity expectedFileMetadata = FileMetadataEntity
                .builder()
                .fileName("test.txt")
                .owner("1111")
                .mimeType("text/plain")
                .path("/dir/text.txt")
                .status(FileStatus.CREATED)
                .fileSize(multipartFile.getSize())
                .build();
        when(fileMetadataRepository.save(any())).thenReturn(expectedFileMetadata);
        when(fileMetadataMapper.entityToResponse(any())).thenReturn(FilePostResponse
                .builder()
                        .status("CREATED")
                        .fileName("test.txt")
                        .mimeType("text/plain")
                .build());
        assertDoesNotThrow(() -> {
            MultipleFilePostResponse response = fileService.uploadFile(List.of(multipartFile), "1111");
            verify(fileRepository, times(1)).saveFile(any());
            verify(fileMetadataRepository, times(1)).save(any());
            assertEquals(1, response.getUploadedFiles().size());
            assertEquals("test.txt", response.getUploadedFiles().getFirst().getFileName());
            assertEquals("CREATED", response.getUploadedFiles().getFirst().getStatus());
            assertEquals("text/plain", response.getUploadedFiles().getFirst().getMimeType());
        });
    }
}