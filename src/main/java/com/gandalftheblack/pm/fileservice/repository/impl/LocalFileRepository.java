package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.repository.FileRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
@Profile("local")
public class LocalFileRepository implements FileRepository {
    @Override
    public String saveFile(File file) {
        return file.getAbsolutePath();
    }
}
