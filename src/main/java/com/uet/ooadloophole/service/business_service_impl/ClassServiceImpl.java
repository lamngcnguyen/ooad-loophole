package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.ClassRepository;
import com.uet.ooadloophole.database.IterationRepository;
import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.model.business.Group;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private GroupService groupService;

    @Override
    public List<Class> getAll() {
        return classRepository.findAll();
    }

    @Override
    public List<Class> getByTeacherId(String teacherId) throws BusinessServiceException {
        List<Class> result = classRepository.findAllByTeacherId(teacherId);
        if (result == null) {
            throw new BusinessServiceException("No class found for this id");
        }
        return result;
    }

    @Override
    public List<Class> getByTeacherIdAndSemesterId(String teacherId, String semesterId) {
        return classRepository.findAllByTeacherIdAndSemesterId(teacherId, semesterId);
    }

    @Override
    public Class getById(String id) throws BusinessServiceException {
        Class result = classRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No class found for this id");
        }
        return result;
    }

    @Override
    public Class getByTeacherIdAndClassName(String teacherId, String name) throws BusinessServiceException {
        Class result = classRepository.getByTeacherIdAndClassName(teacherId, name);
        if (result == null) {
            throw new BusinessServiceException("No class of name keyword found for this teacher");
        }
        return result;
    }

    @Override
    public Class create(Class ooadClass) {
        ooadClass.setActive(true);
        classRepository.save(ooadClass);
        return ooadClass;
    }

    @Override
    public List<Student> getAllStudents(String classId) {
        return studentService.getByClass(classId);
    }

    @Override
    public void delete(String classId) throws BusinessServiceException {
        try {
            Class classToDelete = getById(classId);
            classRepository.delete(classToDelete);

            //TODO: Delete Student by classId
//        List<Student> students = studentRepository.deleteAllByClassId(classId);
//        students.forEach(student -> userRepository.deleteById(student.getUserId()));

            groupService.deleteAllByClassId(classId);

//            List<Topic> topics = topicRepository.deleteAllByClassId(classId);
//            topics.forEach(topic -> {
//                topic.getSpecificationFiles().forEach(userFile -> {
//                    userFileRepository.deleteById(userFile.get_id());
//                });
//            });

//            iterationRepository.deleteAllByClassId(classId);
            fileService.deleteDirectory("repo/" + classId);
        } catch (Exception e) {
            throw new BusinessServiceException("Unable to delete class: " + e.getMessage());
        }
    }

    @Override
    public Class update(String id, Class ooadClass) throws BusinessServiceException {
        try {
            Class dbClass = getById(id);
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
        ArrayList<Student> newStudentList = new ArrayList<>();
        students.forEach(student -> {
            try {
                student.setClassId(classId);
                if (groupService.findOneByName(student.getGroupName()) == null) {
                    Group group = new Group();
                    group.setGroupName(student.getGroupName());
                    group.setClassId(classId);
                    student.setGroupId(group.get_id());
                } else {
                    student.setGroupId(groupService.findOneByName(student.getGroupName()).get_id());
                }
//                createdStudents.add(studentService.create(student));
                newStudentList.add(studentService.create(student));
            } catch (BusinessServiceException e) {
//                students.remove(student);
            }
        });
        return newStudentList;
    }

    @Override
    public List<Class> searchByName(String className) {
        return classRepository.findAllByClassNameLikeIgnoreCase(className);
    }

    @Override
    public boolean classNameExists(String teacherId, String className) {
        return !classRepository.findAllByTeacherIdAndClassNameLikeIgnoreCase(teacherId, className).isEmpty();
    }
}
