package com.gandalftheblack.pm.fileservice.repository;

import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadataEntity, String> {
}
