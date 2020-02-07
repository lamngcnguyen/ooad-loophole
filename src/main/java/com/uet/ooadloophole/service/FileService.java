package com.uet.ooadloophole.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
public interface FileService {

    Path createPath(String dir);

    boolean editFileName(String oldFilePath, String newFilePath);

    boolean deleteFile(String filePath) throws IOException;

    String storeFile(MultipartFile file, String saveLocation);

    Resource loadFileAsResource(String fileName, String saveLocation);

    boolean deleteDirectory(String dir);
}
