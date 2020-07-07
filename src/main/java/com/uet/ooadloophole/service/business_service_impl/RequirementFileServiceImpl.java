package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.requirement_repositories.RequirementSpecFileRepository;
import com.uet.ooadloophole.database.requirement_repositories.RequirementsRepository;
import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import com.uet.ooadloophole.model.business.system_elements.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.RequirementFileService;
import com.uet.ooadloophole.service.business_service.RequirementLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequirementFileServiceImpl implements RequirementFileService {
    @Autowired
    private ConverterService converterService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RequirementSpecFileRepository requirementSpecFileRepository;
    @Autowired
    private RequirementsRepository requirementsRepository;
    @Autowired
    private RequirementLogService requirementLogService;

    @Override
    public RequirementSpecFile findById(String id) {
        return requirementSpecFileRepository.findBy_id(id);
    }

    @Override
    public void updateRequirementId(String specFileId, String requirementId) throws BusinessServiceException {
        RequirementSpecFile dbRequirementSpecFile = findById(specFileId);
        String newPath = Constants.REQ_FOLDER + requirementId + "/";
        dbRequirementSpecFile.setRequirementId(requirementId);
        String fileName = converterService.formatFileName(dbRequirementSpecFile.getFileName(), dbRequirementSpecFile.getFileTimeStamp(), dbRequirementSpecFile.getFileExtension());
        try {
            fileService.moveFile(fileName, dbRequirementSpecFile.getPath(), newPath);
        } catch (BusinessServiceException | IOException e) {
            e.printStackTrace();
            throw new BusinessServiceException("Unable to assign requirementId to this file: " + e.getMessage());
        }
        dbRequirementSpecFile.setPath(newPath);
        requirementSpecFileRepository.save(dbRequirementSpecFile);
    }

    @Override
    public RequirementSpecFile upload(MultipartFile file, String id) {
        try {
            Requirement requirement = requirementsRepository.findBy_id(id);
            List<RequirementSpecFile> files;
            if(requirement.getRequirementSpecFile() == null) files = new ArrayList<>();
            else files = requirement.getRequirementSpecFile();

            String saveLocation = Constants.REQ_FOLDER + "temp/";
            RequirementSpecFile requirementSpecFile = new RequirementSpecFile();
            UserFile userFile = fileService.storeFile(file, saveLocation);

            requirementSpecFile.setFileName(userFile.getFileName());
            requirementSpecFile.setFileExtension(userFile.getFileExtension());
            requirementSpecFile.setTimeStamp(userFile.getTimeStamp());
            requirementSpecFile.setFileTimeStamp(userFile.getFileTimeStamp());
            requirementSpecFile.setUploaderId(userFile.getUploaderId());
            requirementSpecFile.setPath(saveLocation);
            requirementSpecFile.setLatestVersion(true);

            RequirementSpecFile saved = requirementSpecFileRepository.save(requirementSpecFile);
            files.add(saved);
            requirement.setRequirementSpecFile(files);
            requirementsRepository.save(requirement);

            requirementLogService.createLog(requirement,"Uploaded "
                    + requirementSpecFile.getFileName(), "File uploaded");
            return saved;
        } catch (FileStorageException | BusinessServiceException e) {
            throw new FileStorageException("Unable to upload file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String id) {
        RequirementSpecFile requirementSpecFile = findById(id);
        String saveLocation = requirementSpecFile.getPath();
        String fileName = converterService.formatFileName(requirementSpecFile.getFileName(), requirementSpecFile.getFileTimeStamp(), requirementSpecFile.getFileExtension());
        return fileService.loadFileAsResource(fileName, saveLocation);
    }

    @Override
    public RequirementSpecFile deleteFile(String id) throws IOException, BusinessServiceException {
        RequirementSpecFile requirementSpecFile = requirementSpecFileRepository.findBy_id(id);
        requirementSpecFile.setDeleted(true);
        return requirementSpecFileRepository.save(requirementSpecFile);
    }
}
