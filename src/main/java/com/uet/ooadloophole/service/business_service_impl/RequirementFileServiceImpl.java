package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.RequirementSpecFileRepository;
import com.uet.ooadloophole.model.business.RequirementSpecFile;
import com.uet.ooadloophole.model.business.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.RequirementFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RequirementFileServiceImpl implements RequirementFileService {
    @Autowired
    private ConverterService converterService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RequirementSpecFileRepository requirementSpecFileRepository;

    @Override
    public void updateRequirementId(String specFileId, String requirementId) throws BusinessServiceException {
        RequirementSpecFile dbRequirementSpecFile = requirementSpecFileRepository.findBy_id(specFileId);
        String newPath = Constants.SPEC_FOLDER + requirementId + "/";
        dbRequirementSpecFile.setRequirementId(requirementId);
        //specFile.getPath() may indicate the spec file is in temp folder
        String fileName = converterService.formatFileName(dbRequirementSpecFile.getFileName(), dbRequirementSpecFile.getTimeStamp(), dbRequirementSpecFile.getFileExtension());
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
    public RequirementSpecFile upload(MultipartFile file) throws BusinessServiceException {
        try {
            String saveLocation = Constants.SPEC_FOLDER + "temp/";
            RequirementSpecFile requirementSpecFile = new RequirementSpecFile();
            UserFile userFile = fileService.storeFile(file, saveLocation);

            requirementSpecFile.setFileName(userFile.getFileName());
            requirementSpecFile.setFileExtension(userFile.getFileExtension());
            requirementSpecFile.setTimeStamp(userFile.getTimeStamp());
            requirementSpecFile.setUploaderId(userFile.getUploaderId());
            requirementSpecFile.setPath(saveLocation);

            requirementSpecFileRepository.save(requirementSpecFile);
            return requirementSpecFile;
        } catch (FileStorageException | BusinessServiceException e) {
            throw new FileStorageException("Unable to upload file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String id) {
        RequirementSpecFile requirementSpecFile = requirementSpecFileRepository.findBy_id(id);
        String saveLocation = requirementSpecFile.getPath();
        String fileName = converterService.formatFileName(requirementSpecFile.getFileName(), requirementSpecFile.getTimeStamp(), requirementSpecFile.getFileExtension());
        return fileService.loadFileAsResource(fileName, saveLocation);
    }
}
