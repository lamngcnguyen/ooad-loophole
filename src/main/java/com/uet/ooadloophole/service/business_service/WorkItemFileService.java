package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.WorkItemFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface WorkItemFileService {
    WorkItemFile upload(MultipartFile file, String workItemId) throws BusinessServiceException;

    Resource download(String id);

    WorkItemFile getById(String id);

    boolean deleteFile(String id) throws IOException;
}
