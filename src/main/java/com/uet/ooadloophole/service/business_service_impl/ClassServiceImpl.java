package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.ClassRepository;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.interface_model.IClass;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.FileStorageService;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;

    @Override
    public Class getById(String id) throws BusinessServiceException {
        Class result = classRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No class found for this id");
        }
        return result;
    }

    @Override
    public Class create(IClass iClass) {
        Class ooadClass = new Class();
        ooadClass.setClassName(iClass.getClassName());
        ooadClass.setScheduledDayOfWeek(iClass.getScheduledDayOfWeek());
        ooadClass.setSemesterId(iClass.getSemesterId());
        ooadClass.setTeacherId(iClass.getTeacherId());
        classRepository.save(ooadClass);
        return ooadClass;
    }

    @Override
    public void delete(String classId) throws BusinessServiceException {
        try {
            Class classToDelete = getById(classId);
            classRepository.delete(classToDelete);

            //TODO: Delete Student by classId
//        List<Student> students = studentRepository.deleteAllByClassId(classId);
//        students.forEach(student -> userRepository.deleteById(student.getUserId()));

            groupRepository.deleteAllByClassId(classId);

//            List<Topic> topics = topicRepository.deleteAllByClassId(classId);
//            topics.forEach(topic -> {
//                topic.getSpecificationFiles().forEach(userFile -> {
//                    userFileRepository.deleteById(userFile.get_id());
//                });
//            });

            iterationRepository.deleteAllByClassId(classId);
            fileStorageService.deleteDirectory("repo/" + classId);
        } catch (Exception e) {
            throw new BusinessServiceException("Unable to delete class: " + e.getMessage());
        }
    }

    @Override
    public Class update(Class ooadClass) throws BusinessServiceException {
        try {
            Class dbClass = getById(ooadClass.get_id());
            dbClass.setClassName(ooadClass.getClassName());
            dbClass.setScheduledDayOfWeek(ooadClass.getScheduledDayOfWeek());
            dbClass.setSemesterId(ooadClass.getSemesterId());
            dbClass.setTeacherId(ooadClass.getTeacherId());
            classRepository.save(dbClass);
            return dbClass;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update class: " + e.getMessage());
        }
    }

    @Override
    public List<Student> importStudents(String classId, List<Student> students) {
        students.forEach(student -> {
            try {
                student.setClassId(classId);
                studentService.create(student);
            } catch (BusinessServiceException e) {
                students.remove(student);
            }
        });
        return students;
    }
}
