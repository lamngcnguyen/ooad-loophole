package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.SpecFileRepository;
import com.uet.ooadloophole.database.TopicRepository;
import com.uet.ooadloophole.model.business.SpecFile;
import com.uet.ooadloophole.model.business.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.SpecFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class SpecFileServiceImpl implements SpecFileService {
    @Autowired
    private ConverterService converterService;
    @Autowired
    private FileService fileService;
    @Autowired
    private SpecFileRepository specFileRepository;

    @Override
    public SpecFile findById(String id) {
        return specFileRepository.findBy_id(id);
    }

    @Override
    public void updateTopicId(SpecFile specFile, String topicId) throws BusinessServiceException, IOException {
        SpecFile dbSpecFile = findById(specFile.get_id());
        String newPath = Constants.SPEC_FOLDER + specFile.getTopicId() + "/";
        dbSpecFile.setTopicId(topicId);
        dbSpecFile.setPath(newPath);
        //specFile.getPath() may indicate the spec file is in temp folder
        String fileName = converterService.formatFileName(specFile.getFileName(), specFile.getTimeStamp(), specFile.getFileExtension());
        if (!fileService.moveFile(fileName, specFile.getPath(), newPath)) {
            throw new BusinessServiceException("Unable to assign topicId to this file");
        }
        specFileRepository.save(dbSpecFile);
    }

    @Override
    public SpecFile upload(MultipartFile file) throws FileStorageException {
        try {
            String saveLocation = Constants.SPEC_FOLDER + "temp/";
            SpecFile specFile = new SpecFile();
            UserFile userFile = fileService.storeFile(file, saveLocation);

            specFile.setFileName(userFile.getFileName());
            specFile.setFileExtension(userFile.getFileExtension());
            specFile.setTimeStamp(userFile.getTimeStamp());
            specFile.setUploaderId(userFile.getUploaderId());
            specFile.setPath(saveLocation);

            specFileRepository.save(specFile);
            return specFile;
        } catch (FileStorageException | BusinessServiceException e) {
            throw new FileStorageException("Unable to upload repo file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String id) {
        SpecFile specFile = findById(id);
        String saveLocation = specFile.getPath();
        String fileName = converterService.formatFileName(specFile.getFileName(), specFile.getTimeStamp(), specFile.getFileExtension());
        return fileService.loadFileAsResource(fileName, saveLocation);
    }

    @Override
    public List<SpecFile> getByTopicId(String topicId) {
        return specFileRepository.findAllByTopicId(topicId);
    }
}
