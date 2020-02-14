package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.RepoFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface RepoFileService {
    void upload(MultipartFile file, String path) throws BusinessServiceException;

    Resource download(String fileName, String path) throws BusinessServiceException;

    void score(RepoFile repoFile, int score);
}
