package com.gandalftheblack.pm.fileservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FileListGetResponse {
    List<FileGetResponse> files;
}
