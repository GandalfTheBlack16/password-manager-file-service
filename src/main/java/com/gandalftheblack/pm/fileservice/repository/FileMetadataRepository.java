package com.gandalftheblack.pm.fileservice.repository;

import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadataEntity, String> {
    List<FileMetadataEntity> findAllByOwnerAndStatusIn(String owner, List<FileStatus> status);
}
