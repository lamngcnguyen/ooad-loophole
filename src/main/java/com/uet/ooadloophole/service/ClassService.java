package com.uet.ooadloophole.service;

import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.implement_model.IClass;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ClassService {
    Class createClass(IClass iClass);

    void deleteClass(String classId);

    void importStudents(String classId, Map<String, Object> payload) throws Exception;
}
