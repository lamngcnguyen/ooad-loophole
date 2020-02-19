package com.uet.ooadloophole;

import com.uet.ooadloophole.database.NavigationGroupRepository;
import com.uet.ooadloophole.database.NavigationItemRepository;
import com.uet.ooadloophole.database.RoleRepository;
import com.uet.ooadloophole.model.frontend_element.NavigationGroup;
import com.uet.ooadloophole.model.frontend_element.NavigationItem;
import com.uet.ooadloophole.model.business.Role;
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
    CommandLineRunner init(RoleRepository roleRepository, NavigationItemRepository navigationItemRepository, NavigationGroupRepository navigationGroupRepository) {
        return args -> {
            Role adminRole = roleRepository.findByRole("ADMIN");
            if (adminRole == null) {
                Role newAdminRole = new Role();
                newAdminRole.setRole("ADMIN");
                roleRepository.save(newAdminRole);
            } else {
                NavigationItem itemSiteConfig = navigationItemRepository.findByName("Cài đặt hệ thống");
                if (itemSiteConfig == null) {
                    itemSiteConfig = new NavigationItem();
                    itemSiteConfig.setName("Thiết lập hệ thống");
                    itemSiteConfig.setUrl("/admin/business");
                    itemSiteConfig.setRoleId(adminRole.getId());
                    itemSiteConfig.setDescription("Quản lý các thông tin chung của hệ thống, VD: học kỳ, bảng tin...");
                    navigationItemRepository.save(itemSiteConfig);
                }
                NavigationItem itemUser = navigationItemRepository.findByName("Quản lý người dùng");
                if (itemUser == null) {
                    itemUser = new NavigationItem();
                    itemUser.setName("Quản lý người dùng");
                    itemUser.setUrl("/admin/user");
                    itemUser.setRoleId(adminRole.getId());
                    navigationItemRepository.save(itemUser);
                }

                NavigationGroup adminGroup = navigationGroupRepository.findByRoleId(adminRole.getId());
                if (adminGroup == null) {
                    adminGroup = new NavigationGroup();
                    adminGroup.setName("Quản trị viên");
                    List<NavigationItem> items = new ArrayList<>();
                    items.add(itemSiteConfig);
                    items.add(itemUser);
                    adminGroup.setItems(items);
                    adminGroup.setRoleId(adminRole.getId());
                    adminGroup.setUrl("/admin");
                    navigationGroupRepository.save(adminGroup);
                }
            }

            Role userRole = roleRepository.findByRole("USER");
            if (userRole == null) {
                Role newUserRole = new Role();
                newUserRole.setRole("USER");
                roleRepository.save(newUserRole);
            }

            Role teacherRole = roleRepository.findByRole("TEACHER");
            if (teacherRole == null) {
                Role newUserRole = new Role();
                newUserRole.setRole("TEACHER");
                roleRepository.save(newUserRole);
            } else {
                NavigationItem itemClass = navigationItemRepository.findByName("Lớp học của tôi");
                if (itemClass == null) {
                    itemClass = new NavigationItem();
                    itemClass.setName("Lớp học của tôi");
                    itemClass.setUrl("/teacher/class");
                    itemClass.setRoleId(teacherRole.getId());
                    navigationItemRepository.save(itemClass);
                }

                NavigationItem itemEvaluation = navigationItemRepository.findByName("Chấm bài");
                if (itemEvaluation == null) {
                    itemEvaluation = new NavigationItem();
                    itemEvaluation.setName("Chấm bài");
                    itemEvaluation.setUrl("/teacher/evaluate");
                    itemEvaluation.setRoleId(teacherRole.getId());
                    navigationItemRepository.save(itemEvaluation);
                }
                NavigationItem itemBusiness = navigationItemRepository.findByName("Thiết lập quy trình phát triển");
                if (itemBusiness == null) {
                    itemBusiness = new NavigationItem();
                    itemBusiness.setName("Thiết lập quy trình phát triển");
                    itemBusiness.setUrl("/teacher/process");
                    itemBusiness.setRoleId(teacherRole.getId());
                    navigationItemRepository.save(itemBusiness);
                }
                NavigationGroup teacherGroup = navigationGroupRepository.findByRoleId(teacherRole.getId());
                if (teacherGroup == null) {
                    teacherGroup = new NavigationGroup();
                    teacherGroup.setName("Giáo viên");
                    List<NavigationItem> items = new ArrayList<>();
                    items.add(itemClass);
                    items.add(itemEvaluation);
                    items.add(itemBusiness);
                    teacherGroup.setItems(items);
                    teacherGroup.setRoleId(teacherRole.getId());
                    teacherGroup.setUrl("/teacher");
                    navigationGroupRepository.save(teacherGroup);
                }
            }
        };

    }
}
