package com.gandalftheblack.pm.fileservice.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "fileMetadata")
@Data
public class FileMetadataEntity {
    @Id
    private String id;
    private String objectId;
    private String fileName;
    private String mimeType;
    private String path;
    private String owner;
    private FileStatus status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;
}
