package com.gandalftheblack.pm.fileservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipleFilePostResponse {
    List<FilePostResponse> uploadedFiles;
}
