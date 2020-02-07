package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.RepoFileRepository;
import com.uet.ooadloophole.model.RepoFile;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.UserFile;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.CustomFileNotFoundException;
import com.uet.ooadloophole.service.business_service.FileStorageService;
import com.uet.ooadloophole.service.business_service.RepoFileService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

public class RepoFileServiceImpl implements RepoFileService {
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private RepoFileRepository repoFileRepository;

    @Override
    public void upload(MultipartFile file, String path) throws BusinessServiceException {
        try {
            String saveLocation;
            RepoFile repoFile = new RepoFile();
            String userId = secureUserDetailService.getCurrentUser().get_id();
            Student currentStudent = studentService.getStudentById(userId);

            String groupId = currentStudent.getGroupId();
            String classId = currentStudent.getClassId();

            if (path == null) {
                saveLocation = "repo/" + classId + "/" + groupId;
            } else {
                saveLocation = "repo/" + classId + "/" + groupId + "/" + path;
            }
            UserFile userFile = fileStorageService.storeFile(file, saveLocation);

            repoFile.setFileName(userFile.getFileName());
            repoFile.setUploaderId(userFile.getUploaderId());
            repoFile.setPath(saveLocation);
            repoFile.setGroupId(groupId);
            repoFile.setTimeStamp(Instant.now().toString());
            repoFile.setScore(0);
            repoFileRepository.save(repoFile);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to upload repo file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String fileName, String path) throws CustomFileNotFoundException {
        return fileStorageService.loadFileAsResource(fileName, path);
    }

    @Override
    public void score(RepoFile repoFile, int score) {

    }
}
