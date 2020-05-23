package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.rup_elements.Discipline;
import com.uet.ooadloophole.model.business.rup_elements.DisciplineFileType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DisciplineService {
    List<Discipline> getAllDisciplines();
    Discipline getById(String id);
    DisciplineFileType getFileType(String id);
    DisciplineFileType createFileType(DisciplineFileType disciplineFileType);
    DisciplineFileType editFileType(String id, DisciplineFileType disciplineFileType);
    void deleteFileType(String id);
}
