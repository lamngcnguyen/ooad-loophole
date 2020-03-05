package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.model.business.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.CustomFileNotFoundException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private ConverterService converterService;

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
    public boolean editFileName(String saveLocation, String oldFileName, String newFileName) {
        File file = new File(saveLocation + oldFileName);
        return file.renameTo(new File(saveLocation + newFileName));
    }

    @Override
    public void moveFile(String filename, String oldPath, String newPath) throws BusinessServiceException {
        File file = new File(oldPath + filename);
        if(!file.exists()) throw new BusinessServiceException("File not found " + oldPath + filename);
        File saveLocation = new File(newPath);
        if (!saveLocation.exists()) {
            if (!saveLocation.mkdir()) {
                throw new BusinessServiceException("Unable to create path " + newPath);
            }
        }
        if (!file.renameTo(new File(newPath + filename))) {
            throw new BusinessServiceException("unable to move file " + filename);
        }
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
    public UserFile storeFile(MultipartFile file, String saveLocation) throws FileStorageException, BusinessServiceException {
        UserFile uploadedUserFile = new UserFile();
        String userId = secureUserDetailService.getCurrentUser().get_id();

        Path savePath = createPath(saveLocation);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = FilenameUtils.getExtension(originalFileName);
        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            String fileName = converterService.formatFileName(originalFileName, timeStamp, extension);
            assert savePath != null;
            Path targetLocation = savePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            uploadedUserFile.setFileName(originalFileName);
            uploadedUserFile.setFileExtension(extension);
            uploadedUserFile.setTimeStamp(timeStamp);
            uploadedUserFile.setUploaderId(userId);
            uploadedUserFile.setPath(saveLocation);
            return uploadedUserFile;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName, String saveLocation) {
        Path path = createPath(saveLocation);
        try {
            assert path != null;
            Path filePath = path.resolve(fileName).normalize();
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
