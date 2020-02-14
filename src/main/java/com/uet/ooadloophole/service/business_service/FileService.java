package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.UserFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
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

    UserFile storeFile(MultipartFile file, String saveLocation) throws FileStorageException, BusinessServiceException;

    Resource loadFileAsResource(String fileName, String saveLocation);

    boolean deleteDirectory(String dir);
}
