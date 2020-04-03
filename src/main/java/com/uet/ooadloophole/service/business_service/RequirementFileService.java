package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.RequirementSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface RequirementFileService {
    RequirementSpecFile upload(MultipartFile file) throws BusinessServiceException;

    void updateRequirementId(String specFileId, String requirementId) throws BusinessServiceException, IOException;

    Resource download(String id);
}
