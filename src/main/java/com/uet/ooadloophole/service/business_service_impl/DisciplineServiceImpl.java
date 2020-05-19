package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.rup_repositories.DisciplineFileTypeRepository;
import com.uet.ooadloophole.database.rup_repositories.DisciplineRepository;
import com.uet.ooadloophole.model.business.rup_elements.Discipline;
import com.uet.ooadloophole.model.business.rup_elements.DisciplineFileType;
import com.uet.ooadloophole.service.business_service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineServiceImpl implements DisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private DisciplineFileTypeRepository disciplineFileTypeRepository;

    @Override
    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    @Override
    public Discipline getById(String id) {
        return disciplineRepository.findBy_id(id);
    }

    @Override
    public DisciplineFileType getFileType(String id) {
        return disciplineFileTypeRepository.findBy_id(id);
    }

    @Override
    public DisciplineFileType createFileType(DisciplineFileType disciplineFileType) {
        DisciplineFileType newFileType = new DisciplineFileType();
        newFileType.setName(disciplineFileType.getName());
        newFileType.setDisciplineName(disciplineFileType.getDisciplineName());
        newFileType.setFileType(disciplineFileType.getFileType());
        return disciplineFileTypeRepository.save(newFileType);
    }

    @Override
    public DisciplineFileType editFileType(String id, DisciplineFileType disciplineFileType) {
        DisciplineFileType dbFileType = disciplineFileTypeRepository.findBy_id(id);
        dbFileType.setName(disciplineFileType.getName());
        dbFileType.setDisciplineName(disciplineFileType.getDisciplineName());
        dbFileType.setFileType(disciplineFileType.getFileType());
        return disciplineFileTypeRepository.save(dbFileType);
    }

    @Override
    public void deleteFileType(String id) {
        disciplineFileTypeRepository.delete(getFileType(id));
    }
}
