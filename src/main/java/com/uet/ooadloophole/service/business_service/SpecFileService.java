package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.SpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface SpecFileService {
    SpecFile findById(String id);

    SpecFile upload(MultipartFile file, String topicId) throws BusinessServiceException;

    Resource download(String id);

    List<SpecFile> getByTopicId(String topicId);
}
