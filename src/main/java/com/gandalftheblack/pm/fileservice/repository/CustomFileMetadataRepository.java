package com.gandalftheblack.pm.fileservice.repository;

import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;

import java.util.List;

public interface CustomFileMetadataRepository {
    List<FileMetadataEntity> findAllByOwnerFilteredByStatusAndName(String owner, List<FileStatus> status, String nameFilter);
}
