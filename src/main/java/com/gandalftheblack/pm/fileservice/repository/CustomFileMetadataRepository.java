package com.gandalftheblack.pm.fileservice.repository;

import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFileMetadataRepository {
    Page<FileGetResponse> findAllByOwnerFilteredByStatusAndName(String owner, List<FileStatus> status, String nameFilter, Pageable pageable);
}
