package com.gandalftheblack.pm.fileservice.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultipleFilePostResponse {
  List<FilePostResponse> uploadedFiles;
}
