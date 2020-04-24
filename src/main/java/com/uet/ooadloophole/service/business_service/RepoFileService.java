package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.RepoFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface RepoFileService {
    RepoFile getById(String id);

    RepoFile upload(MultipartFile file, String path) throws BusinessServiceException;

    Resource download(String fileId);

    RepoFile updateFile(MultipartFile file, String previousVersionId) throws BusinessServiceException;

    boolean checkExists(String filename, String groupId, String path);
}
