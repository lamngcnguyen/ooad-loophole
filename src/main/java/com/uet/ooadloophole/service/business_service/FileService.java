package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.system_elements.UserFile;
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

    boolean editFileName(String saveLocation, String oldFileName, String newFileName);

    void moveFile(String filename, String oldPath, String newPath) throws BusinessServiceException, IOException;

    boolean deleteFile(String filePath) throws IOException;

    UserFile storeFile(MultipartFile file, String saveLocation) throws FileStorageException, BusinessServiceException;

    String storeAvatar(MultipartFile file, String saveLocation) throws BusinessServiceException;

    Resource loadFileAsResource(String fileName, String saveLocation);

    boolean deleteDirectory(String dir);


}
