package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.group_repositories.RepoFileRepository;
import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.model.business.system_elements.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.RepoFileService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RepoFileServiceImpl implements RepoFileService {
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RepoFileRepository repoFileRepository;
    @Autowired
    private ConverterService converterService;

    @Override
    public RepoFile getById(String id) {
        return repoFileRepository.findBy_id(id);
    }

    @Override
    public List<RepoFile> getAllByIteration(String iterationId) {
        return repoFileRepository.findAllByIterationId(iterationId);
    }

    @Override
    public RepoFile upload(MultipartFile file, String path, String iterationId) throws BusinessServiceException {
        try {
            String saveLocation;
            String userId = secureUserService.getCurrentUser().get_id();
            Student currentStudent = studentService.getByUserId(userId);
            String groupId = currentStudent.getGroupId();
            String classId = currentStudent.getClassId();

            if (path == null) {
                saveLocation = Constants.REPO_FOLDER + classId + "/" + groupId;
            } else {
                saveLocation = Constants.REPO_FOLDER + classId + "/" + groupId + "/" + path;
            }
            if (checkExists(file.getOriginalFilename(), groupId, saveLocation)) {
                throw new BusinessServiceException("This file name is already exists");
            }
            UserFile userFile = fileService.storeFile(file, saveLocation);
            RepoFile repoFile = new RepoFile();
            repoFile.setFileName(userFile.getFileName());
            repoFile.setFileExtension(userFile.getFileExtension());
            repoFile.setUploaderId(userFile.getUploaderId());
            repoFile.setTimeStamp(userFile.getTimeStamp());
            repoFile.setFileTimeStamp(userFile.getFileTimeStamp());
            repoFile.setPath(path);
            repoFile.setIterationId(iterationId);
            repoFile.setClassId(classId);
            repoFile.setGroupId(groupId);
            return repoFileRepository.save(repoFile);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to upload repo file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String fileId) {
        RepoFile repoFile = getById(fileId);
        String fileName = converterService.formatFileName(repoFile.getFileName(), repoFile.getFileTimeStamp(), repoFile.getFileExtension());
        String path = Constants.REPO_FOLDER + repoFile.getClassId() + "/" + repoFile.getGroupId() + "/" + repoFile.getPath();
        return fileService.loadFileAsResource(fileName, path);
    }

    @Override
    public RepoFile updateFile(MultipartFile file, String previousVersionId) throws BusinessServiceException {
        try {
            RepoFile previousVersion = getById(previousVersionId);
            String saveLocation = previousVersion.getPath();
            String userId = secureUserService.getCurrentUser().get_id();
            Student currentStudent = studentService.getByUserId(userId);
            String groupId = currentStudent.getGroupId();
            String classId = currentStudent.getClassId();

            if (checkExists(file.getOriginalFilename(), groupId, saveLocation)) {
                throw new BusinessServiceException("This file name is already exists");
            }
            UserFile userFile = fileService.storeFile(file, saveLocation);
            RepoFile repoFile = new RepoFile();
            repoFile.setFileName(userFile.getFileName());
            repoFile.setFileExtension(userFile.getFileExtension());
            repoFile.setUploaderId(userFile.getUploaderId());
            repoFile.setTimeStamp(userFile.getTimeStamp());
            repoFile.setFileTimeStamp(userFile.getFileTimeStamp());
            repoFile.setPath(userFile.getPath());
            repoFile.setIterationId(previousVersion.getIterationId());
            repoFile.setClassId(classId);
            repoFile.setGroupId(groupId);
            repoFile.setPreviousVersionId(previousVersionId);
            return repoFileRepository.save(repoFile);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update repo file. " + e.getMessage());
        }
    }

    @Override
    public boolean checkExists(String filename, String groupId, String path) {
        List<RepoFile> repoFiles = repoFileRepository.findAllByGroupIdAndFileNameAndPath(groupId, filename, path);
        return repoFiles.size() != 0;
    }

    @Override
    public RepoFile delete(String id) {
        RepoFile repoFile = getById(id);
        repoFile.setDeleted(true);
        return repoFile;
    }
}
