package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.SpecFileRepository;
import com.uet.ooadloophole.model.business.SpecFile;
import com.uet.ooadloophole.model.business.UserFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.SpecFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SpecFileServiceImpl implements SpecFileService {
    @Autowired
    private FileService fileService;
    @Autowired
    private SpecFileRepository specFileRepository;

    @Override
    public SpecFile findById(String id) {
        return specFileRepository.findBy_id(id);
    }

    @Override
    public SpecFile upload(MultipartFile file, String topicId) throws FileStorageException {
        try {
            String saveLocation = Constants.SPEC_FOLDER + topicId;
            SpecFile specFile = new SpecFile();
            UserFile userFile = fileService.storeFile(file, saveLocation);

            specFile.setFileName(userFile.getFileName());
            specFile.setFileExtension(userFile.getFileExtension());
            specFile.setTimeStamp(userFile.getTimeStamp());
            specFile.setUploaderId(userFile.getUploaderId());
            specFile.setTopicId(topicId);

            specFileRepository.save(specFile);
            return specFile;
        } catch (FileStorageException | BusinessServiceException e) {
            throw new FileStorageException("Unable to upload repo file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String id) {
        SpecFile specFile = findById(id);
        String saveLocation = Constants.SPEC_FOLDER + specFile.getTopicId();
        return fileService.loadFileAsResource(specFile.getFileName(), saveLocation);
    }

    @Override
    public List<SpecFile> getByTopicId(String topicId) {
        return specFileRepository.findAllByTopicId(topicId);
    }
}
