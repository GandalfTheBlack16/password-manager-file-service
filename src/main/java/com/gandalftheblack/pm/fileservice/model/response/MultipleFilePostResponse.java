package com.gandalftheblack.pm.fileservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MultipleFilePostResponse extends HttpBaseResponse{
    List<FilePostResponse> uploadedFiles;
}
