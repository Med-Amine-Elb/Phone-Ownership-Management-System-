package com.phone.config;

import com.phone.model.*;
import com.phone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(
            RoleRepository roleRepository,
            SystemUserRepository systemUserRepository,
            UserRepository userRepository,
            PhoneRepository phoneRepository,
            SimCardRepository simCardRepository,
            PhoneAssignmentRepository phoneAssignmentRepository
    ) {
        return args -> {
            // Create Roles
            Role adminRole = roleRepository.findByName(Role.RoleType.ADMIN).orElseGet(() -> {
                Role role = new Role();
                role.setName(Role.RoleType.ADMIN);
                return roleRepository.save(role);
            });

            Role assignerRole = roleRepository.findByName(Role.RoleType.ASSIGNER).orElseGet(() -> {
                Role role = new Role();
                role.setName(Role.RoleType.ASSIGNER);
                return roleRepository.save(role);
            });

            // Create SystemUsers
            SystemUser admin = new SystemUser();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            systemUserRepository.save(admin);

            SystemUser assigner = new SystemUser();
            assigner.setUsername("assigner");
            assigner.setEmail("assigner@example.com");
            assigner.setPasswordHash(passwordEncoder.encode("assigner123"));
            assigner.setRole(assignerRole);
            systemUserRepository.save(assigner);

            // Create 5 Users
            for (int i = 1; i <= 5; i++) {
                User user = new User();
                user.setFirstName("User" + i);
                user.setLastName("Test");
                user.setEmail("user" + i + "@example.com");
                user.setDepartment("IT");
                user.setStatus(User.UserStatus.ACTIVE);
                userRepository.save(user);
            }

            // Create 5 Phones
            for (int i = 1; i <= 5; i++) {
                Phone phone = new Phone();
                phone.setBrand("Brand" + i);
                phone.setModel("Model" + i);
                phone.setCreatedBy(admin);
                phoneRepository.save(phone);
            }

            // Create 5 SimCards
            for (int i = 1; i <= 5; i++) {
                SimCard simCard = new SimCard();
                simCard.setNumber("100000000" + i);
                simCard.setSsid("SIM" + i);
                simCard.setPinCode("111" + i);
                simCard.setPukCode("222" + i);
                simCard.setForfait("Standard");
                simCard.setOperator("Operator" + i);
                simCard.setCreatedBy(admin);
                simCardRepository.save(simCard);
            }

            // Create a PhoneAssignment by the assigner
            User firstUser = userRepository.findAll().get(0);
            Phone firstPhone = phoneRepository.findAll().get(0);
            SimCard firstSimCard = simCardRepository.findAll().get(0);

            PhoneAssignment assignment = new PhoneAssignment();
            assignment.setUser(firstUser);
            assignment.setPhone(firstPhone);
            assignment.setSimCard(firstSimCard);
            assignment.setPvFileUrl("test.pdf");
            assignment.setStatus(PhoneAssignment.AssignmentStatus.ACTIVE);
            assignment.setAssignedBy(assigner);
            phoneAssignmentRepository.save(assignment);
        };
    }
} 