package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.database.group_repositories.WorkItemFileRepository;
import com.uet.ooadloophole.database.group_repositories.WorkItemRepository;
import com.uet.ooadloophole.model.business.class_elements.TopicSpecFile;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemFile;
import com.uet.ooadloophole.model.business.system_elements.UserFile;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.WorkItemFileService;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class WorkItemFileServiceImpl implements WorkItemFileService {
    @Autowired
    private WorkItemFileRepository workItemFileRepository;
    @Autowired
    private WorkItemRepository workItemRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ConverterService converterService;

    @Override
    public WorkItemFile upload(MultipartFile file, String workItemId) throws BusinessServiceException {
        WorkItem workItem = workItemRepository.findBy_id(workItemId);
        List<WorkItemFile> workItemFiles = workItem.getWorkItemFiles();
        Group group = groupService.getById(workItem.getGroupId());
        String saveLocation = Constants.WORK_ITEM_FOLDER + group.get_id() + "/" + workItemId;

        WorkItemFile workItemFile = new WorkItemFile();
        UserFile userFile = fileService.storeFile(file, saveLocation);
        workItemFile.setFileName(userFile.getFileName());
        workItemFile.setFileExtension(userFile.getFileExtension());
        workItemFile.setTimeStamp(userFile.getTimeStamp());
        workItemFile.setFileTimeStamp(userFile.getFileTimeStamp());
        workItemFile.setUploaderId(userFile.getUploaderId());
        workItemFile.setPath(saveLocation);
        workItemFile.setTaskId(workItemId);

        workItemFiles.add(workItemFile);
        workItem.setWorkItemFiles(workItemFiles);
        workItem.setStatus("Committed");
        workItemRepository.save(workItem);
        return workItemFileRepository.save(workItemFile);
    }

    @Override
    public Resource download(String id) {
        WorkItemFile workItemFile = workItemFileRepository.findBy_id(id);
        String saveLocation = workItemFile.getPath();
        String fileName = converterService.formatFileName(workItemFile.getFileName(), workItemFile.getFileTimeStamp(), workItemFile.getFileExtension());
        return fileService.loadFileAsResource(fileName, saveLocation);
    }

    @Override
    public WorkItemFile getById(String id) {
        return workItemFileRepository.findBy_id(id);
    }
}
