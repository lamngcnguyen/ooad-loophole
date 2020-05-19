package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface RepoFileService {
    RepoFile getById(String id);

    List<RepoFile> getAllByIteration(String iterationId);

    RepoFile upload(MultipartFile file, String path, String iterationId) throws BusinessServiceException;

    Resource download(String fileId);

    RepoFile updateFile(MultipartFile file, String previousVersionId) throws BusinessServiceException;

    boolean checkExists(String filename, String groupId, String path);

    RepoFile delete(String id);
}
