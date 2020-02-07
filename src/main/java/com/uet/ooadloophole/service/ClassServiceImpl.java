package com.uet.ooadloophole.service;

import com.uet.ooadloophole.database.*;
import com.uet.ooadloophole.model.*;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.implement_model.IClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserFileRepository userFileRepository;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @Override
    public Class createClass(IClass iClass) {
        Class ooadClass = new Class();
        ooadClass.setClassName(iClass.getClassName());
        ooadClass.setScheduledDayOfWeek(iClass.getScheduledDayOfWeek());
        ooadClass.setSemesterId(iClass.getSemesterId());
        ooadClass.setTeacherId(secureUserDetailService.getTeacherId());
        classRepository.save(ooadClass);
        return ooadClass;
    }

    @Override
    public void deleteClass(String classId) {
        classRepository.deleteById(classId);

        List<Student> students = studentRepository.deleteAllByClassId(classId);
        students.forEach(student -> userRepository.deleteById(student.getUserId()));

        groupRepository.deleteAllByClassId(classId);

        List<Topic> topics = topicRepository.deleteAllByClassId(classId);
        topics.forEach(topic -> {
            topic.getSpecificationFiles().forEach(userFile -> {
                userFileRepository.deleteById(userFile.get_id());
            });
        });

        iterationRepository.deleteAllByClassId(classId);
        fileService.deleteDirectory("repo/" + classId);
    }

    @Override
    public void importStudents(String classId, Map<String, Object> payload) throws Exception {
        JSONObject jsonObject = new JSONObject(payload);
        Class ooadClass = classRepository.findBy_id(classId);
        if (ooadClass == null) throw new Exception("Class not found!");
        JSONArray array = jsonObject.getJSONArray("students");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (studentRepository.findByStudentId((String) object.get("studentId")) == null) {
                User user = new User();
                Student student = new Student();
                Group group = new Group();

                user.setFullName((String) object.get("fullName"));
                user.setPassword((String) object.get("studentId"));
                user.setEmail((String) object.get("email"));
                userService.saveUser(user, "USER");

                if (groupRepository.findByClassIdAndGroupName(classId, (String) object.get("groupName")) == null) {
                    group.setGroupName((String) object.get("groupName"));
                    group.setClassId(ooadClass.get_id());
                    groupRepository.save(group);
                } else {
                    group = groupRepository.findByGroupName((String) object.get("groupName"));
                }

                student.setUserId(user.get_id());
                student.setClassId(ooadClass.get_id());
                student.setGroupId(group.get_id());
                student.setStudentId((String) object.get("studentId"));
                studentRepository.save(student);
            }
        }
    }
}
