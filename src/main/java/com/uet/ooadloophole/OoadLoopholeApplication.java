package com.uet.ooadloophole;

import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.model.frontend_element.NavigationItem;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.NavigationGroupService;
import com.uet.ooadloophole.service.business_service.NavigationItemService;
import com.uet.ooadloophole.service.business_service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class OoadLoopholeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OoadLoopholeApplication.class, args);
    }

    //Create Role
    @Bean
    CommandLineRunner init(RoleService roleService, NavigationItemService navigationItemService, NavigationGroupService navigationGroupService) {
        return args -> {
            Role adminRole = roleService.getByName("ADMIN");
            if (adminRole == null) {
                roleService.create("ADMIN");
            } else {
                NavigationItem itemSiteConfig = navigationItemService.getByName("Thiết lập hệ thống");
                if (itemSiteConfig == null) {
                    itemSiteConfig = navigationItemService.create("Thiết lập hệ thống", "/admin/business", adminRole,
                            "Quản lý các thông tin chung của hệ thống, VD: học kỳ, bảng tin...");
                }
                NavigationItem itemUser = navigationItemService.getByName("Quản lý người dùng");
                if (itemUser == null) {
                    itemUser = navigationItemService.create("Quản lý người dùng", "/admin/user", adminRole, null);
                }
                NavigationItem itemClass = navigationItemService.getByName("Quản lý lớp học");
                if (itemClass == null) {
                    itemClass = navigationItemService.create("Quản lý lớp học", "/admin/class", adminRole, null);
                }
                NavigationItem itemSemester = navigationItemService.getByName("Quản lý học kì");
                if (itemSemester == null) {
                    itemSemester = navigationItemService.create("Quản lý học kì", "/admin/semester", adminRole, null);
                }

                try {
                    navigationGroupService.getByRole(adminRole);
                } catch (BusinessServiceException ignored) {
                    List<NavigationItem> items = new ArrayList<>();
                    items.add(itemSiteConfig);
                    items.add(itemUser);
                    items.add(itemClass);
                    items.add(itemSemester);
                    navigationGroupService.create("Quản trị viên", items, adminRole, "/admin");
                }
            }

            Role studentRole = roleService.getByName("STUDENT");
            if (studentRole == null) {
                roleService.create("STUDENT");
            } else {
                NavigationItem itemGroup = navigationItemService.getByName("Nhóm");
                if (itemGroup == null) {
                    itemGroup = navigationItemService.create("Nhóm", "/student/group", studentRole, null);
                }
                NavigationItem itemIteration = navigationItemService.getByName("Vòng lặp phát triển");
                if (itemIteration == null) {
                    itemIteration = navigationItemService.create("Vòng lặp phát triển", "/student/iteration", studentRole, null);
                }
                NavigationItem itemEvaluation = navigationItemService.getByName("Chấm điểm");
                if (itemEvaluation == null) {
                    itemEvaluation = navigationItemService.create("Chấm điểm", "/student/evaluation", studentRole, null);
                }
                NavigationItem itemRequirement = navigationItemService.getByName("Yêu cầu");
                if (itemRequirement == null) {
                    itemRequirement = navigationItemService.create("Yêu cầu", "/student/requirement", studentRole, null);
                }
                try {
                    navigationGroupService.getByRole(studentRole);
                } catch (BusinessServiceException ignored) {
                    List<NavigationItem> items = new ArrayList<>();
                    items.add(itemGroup);
                    items.add(itemIteration);
                    items.add(itemEvaluation);
                    items.add(itemRequirement);
                    navigationGroupService.create("Sinh viên", items, studentRole, "/student");
                }
            }

            Role teacherRole = roleService.getByName("TEACHER");
            if (teacherRole == null) {
                roleService.create("TEACHER");
            } else {
                NavigationItem itemClass = navigationItemService.getByName("Lớp học của tôi");
                if (itemClass == null) {
                    itemClass = navigationItemService.create("Lớp học của tôi", "/teacher/class", teacherRole, null);
                }

                NavigationItem itemEvaluation = navigationItemService.getByName("Chấm bài");
                if (itemEvaluation == null) {
                    itemEvaluation = navigationItemService.create("Chấm bài", "/teacher/evaluate", teacherRole, null);
                }
                NavigationItem itemBusiness = navigationItemService.getByName("Thiết lập quy trình phát triển");
                if (itemBusiness == null) {
                    itemBusiness = navigationItemService.create("Thiết lập quy trình phát triển", "/teacher/process", teacherRole, null);
                }
                try {
                    navigationGroupService.getByRole(teacherRole);
                } catch (BusinessServiceException ignored) {
                    List<NavigationItem> items = new ArrayList<>();
                    items.add(itemClass);
                    items.add(itemEvaluation);
                    items.add(itemBusiness);
                    navigationGroupService.create("Giáo viên", items, teacherRole, "/teacher");
                }
            }
        };
    }
}
