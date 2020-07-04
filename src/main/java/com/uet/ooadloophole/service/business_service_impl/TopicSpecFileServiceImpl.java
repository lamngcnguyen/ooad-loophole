package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.class_repositories.TopicSpecFileRepository;
import com.uet.ooadloophole.model.business.class_elements.TopicSpecFile;
import com.uet.ooadloophole.model.business.system_elements.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_exceptions.FileStorageException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.TopicSpecFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TopicSpecFileServiceImpl implements TopicSpecFileService {
    @Autowired
    private ConverterService converterService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TopicSpecFileRepository specFileRepository;

    @Override
    public TopicSpecFile findById(String id) {
        return specFileRepository.findBy_id(id);
    }

    @Override
    public void updateTopicId(String specFileId, String topicId) throws BusinessServiceException {
        TopicSpecFile dbTopicSpecFile = findById(specFileId);
        String newPath = Constants.SPEC_FOLDER + topicId + "/";
        dbTopicSpecFile.setTopicId(topicId);
        //specFile.getPath() may indicate the spec file is in temp folder
        String fileName = converterService.formatFileName(dbTopicSpecFile.getFileName(), dbTopicSpecFile.getFileTimeStamp(), dbTopicSpecFile.getFileExtension());
        try {
            fileService.moveFile(fileName, dbTopicSpecFile.getPath(), newPath);
        } catch (BusinessServiceException | IOException e) {
            e.printStackTrace();
            throw new BusinessServiceException("Unable to assign topicId to this file: " + e.getMessage());
        }
        dbTopicSpecFile.setPath(newPath);
        specFileRepository.save(dbTopicSpecFile);
    }

    @Override
    public TopicSpecFile upload(MultipartFile file) throws FileStorageException {
        try {
            String saveLocation = Constants.SPEC_FOLDER + "temp/";
            TopicSpecFile topicSpecFile = new TopicSpecFile();
            UserFile userFile = fileService.storeFile(file, saveLocation);

            topicSpecFile.setFileName(userFile.getFileName());
            topicSpecFile.setFileExtension(userFile.getFileExtension());
            topicSpecFile.setTimeStamp(userFile.getTimeStamp());
            topicSpecFile.setFileTimeStamp(userFile.getFileTimeStamp());
            topicSpecFile.setUploaderId(userFile.getUploaderId());
            topicSpecFile.setPath(saveLocation);

            return specFileRepository.save(topicSpecFile);
        } catch (FileStorageException | BusinessServiceException e) {
            throw new FileStorageException("Unable to upload repo file. " + e.getMessage());
        }
    }

    @Override
    public Resource download(String id) {
        TopicSpecFile topicSpecFile = findById(id);
        String saveLocation = topicSpecFile.getPath();
        String fileName = converterService.formatFileName(topicSpecFile.getFileName(), topicSpecFile.getFileTimeStamp(), topicSpecFile.getFileExtension());
        return fileService.loadFileAsResource(fileName, saveLocation);
    }

    @Override
    public List<TopicSpecFile> getByTopicId(String topicId) {
        return specFileRepository.findAllByTopicId(topicId);
    }

    @Override
    public boolean delete(String id) throws IOException {
        TopicSpecFile topicSpecFile = specFileRepository.findBy_id(id);
        String fileName = converterService.formatFileName(topicSpecFile.getFileName(), topicSpecFile.getFileTimeStamp(), topicSpecFile.getFileExtension());
        String dir = topicSpecFile.getPath() + "/" + fileName;
        return fileService.deleteFile(dir);
    }
}
