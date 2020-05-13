package com.uet.ooadloophole.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uet.ooadloophole.database.DisciplineFileTypeRepository;
import com.uet.ooadloophole.database.DisciplineRepository;
import com.uet.ooadloophole.model.business.Discipline;
import com.uet.ooadloophole.model.business.DisciplineFileType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DisciplineSetup {
    private String phaseConfigFile;
    private DisciplineRepository disciplineRepository;
    private DisciplineFileTypeRepository disciplineFileTypeRepository;

    public DisciplineSetup(String phaseConfigFile, DisciplineRepository disciplineRepository, DisciplineFileTypeRepository disciplineFileTypeRepository) {
        this.phaseConfigFile = phaseConfigFile;
        this.disciplineRepository = disciplineRepository;
        this.disciplineFileTypeRepository = disciplineFileTypeRepository;
    }

    public void createDisciplines() throws FileNotFoundException {
        JsonArray disciplinesArray = JsonParser.parseReader(new FileReader(phaseConfigFile)).getAsJsonArray();
        disciplinesArray.forEach(d -> {
            Discipline discipline = disciplineRepository.findByName(d.getAsJsonObject().get("name").getAsString());
            if (discipline == null) {
                Discipline newDiscipline = new Discipline();
                newDiscipline.setName(d.getAsJsonObject().get("name").getAsString());
                newDiscipline.setDescription(d.getAsJsonObject().get("description").getAsString());
                List<DisciplineFileType> items = new ArrayList<>();
                d.getAsJsonObject().get("types").getAsJsonArray().forEach(i -> items.add(createFileType(i.getAsJsonObject(), d.getAsJsonObject().get("name").getAsString())));
                items.forEach(item -> {
                    DisciplineFileType disciplineFileType = disciplineFileTypeRepository.findByName(item.getName());
                    if (disciplineFileType == null) {
                        DisciplineFileType newFileType = new DisciplineFileType();
                        newFileType.setName(item.getName());
                        newFileType.setDisciplineName(item.getDisciplineName());
                        newFileType.setFileType(item.getFileType());
                        disciplineFileTypeRepository.save(newFileType);
                    }
                });
                disciplineRepository.save(newDiscipline);
            }
        });
    }

    private DisciplineFileType createFileType(JsonObject data, String disciplineName) {
        DisciplineFileType file = new DisciplineFileType();
        file.setName(data.get("name").getAsString());
        file.setDisciplineName(disciplineName);
        file.setFileType(data.get("fileType").getAsString());
        return file;
    }
}
