package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.model.UserFile;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.CustomFileNotFoundException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileStorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageStorageServiceImpl implements FileStorageService {
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @Override
    public Path createPath(String dir) {
        try {
            Path path = Paths.get(dir).toAbsolutePath().normalize();
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean editFileName(String oldFilePath, String newFilePath) {
        File file = new File(oldFilePath);
        return file.renameTo(new File(file.getParentFile(), newFilePath));
    }

    @Override
    public boolean deleteFile(String filePath) throws IOException {
        File fileToDelete = new File(filePath);
        if (fileToDelete.isDirectory()) {
            FileUtils.deleteDirectory(fileToDelete);
            return true;
        } else {
            return fileToDelete.delete();
        }
    }

    @Override
    public UserFile storeFile(MultipartFile file, String saveLocation) throws FileStorageException {
        UserFile uploadedUserFile = new UserFile();
        String userId = secureUserDetailService.getCurrentUser().get_id();

        Path savePath = createPath(saveLocation);
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            assert savePath != null;
            Path targetLocation = savePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            uploadedUserFile.setFileName(fileName);
            uploadedUserFile.setUploaderId(userId);
            return uploadedUserFile;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }

    @Override
    public Resource loadFileAsResource(String fileName, String saveLocation) throws CustomFileNotFoundException {
        Path path = createPath(saveLocation);
        try {
            assert path != null;
            Path filePath = path.resolve(fileName).normalize();
            System.out.println(filePath.toAbsolutePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new CustomFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new CustomFileNotFoundException("File not found " + fileName, ex);
        }
    }

    @Override
    public boolean deleteDirectory(String dir) {
        Path path = Paths.get(dir).toAbsolutePath();
        return deleteDirectory(new File(path.toString()));
    }

    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            assert children != null;
            for (File child : children) {
                boolean success = deleteDirectory(child);
                if (!success) return false;
            }
        }
        return dir.delete();
    }
}
