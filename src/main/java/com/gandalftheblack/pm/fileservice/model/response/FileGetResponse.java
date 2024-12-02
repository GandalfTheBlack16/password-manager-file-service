package com.gandalftheblack.pm.fileservice.model.response;

import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FileGetResponse {
    private String fileId;
    private String fileName;
    private String mimeType;
    private Long fileSize;
    private String path;
    private FileStatus status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;
}
