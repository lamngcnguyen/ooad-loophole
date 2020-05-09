package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.TopicSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface TopicSpecFileService {
    TopicSpecFile findById(String id);

    void updateTopicId(String specFileId, String topicId) throws BusinessServiceException, IOException;

    TopicSpecFile upload(MultipartFile file) throws BusinessServiceException;

    Resource download(String id);

    List<TopicSpecFile> getByTopicId(String topicId);

    boolean delete(String id) throws IOException;
}
