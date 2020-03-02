package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.SpecFileRepository;
import com.uet.ooadloophole.database.TopicRepository;
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

import java.time.Instant;
import java.util.List;

@Service
public class SpecFileServiceImpl implements SpecFileService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private SpecFileRepository specFileRepository;

    @Override
    public void upload(MultipartFile file, String path, String topicId) throws FileStorageException {
        try {
            String saveLocation;
            SpecFile specFile = new SpecFile();
            String classId = topicRepository.findBy_id(topicId).getClassId();

            if (path == null) {
                saveLocation = "repo/" + classId + "/" + topicId;
            } else {
                saveLocation = "repo/" + classId + "/" + topicId + "/" + path;
            }
            UserFile userFile = fileService.storeFile(file, saveLocation);

            specFile.setFileName(userFile.getFileName());
            specFile.setUploaderId(userFile.getUploaderId());
            specFile.setPath(saveLocation);
            specFile.setTimeStamp(Instant.now().toString());
            specFileRepository.save(specFile);
        } catch (FileStorageException | BusinessServiceException e) {
            throw new FileStorageException("Unable to upload repo file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String fileName, String path) throws BusinessServiceException {
        return null;
    }

    @Override
    public List<SpecFile> getByTopicId(String topicId) {
        return specFileRepository.findAllByTopicId(topicId);
    }
}
