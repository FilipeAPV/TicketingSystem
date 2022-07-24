package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.constants.Constants;
import com.mycompany.ticketingsystem.controller.ListTicketsController;
import com.mycompany.ticketingsystem.dto.SuperUserDTO;
import com.mycompany.ticketingsystem.model.Department;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.DepartmentRepository;
import com.mycompany.ticketingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
    }

    public Boolean saveUser(User user) {
        boolean isSaved = false;

        //Hash the plain text password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        if (user != null && user.getId() > 0) {
            isSaved = true;
        }

        return isSaved;
    }

    public int totalNumberOfUsers() {
        List<User> userList = userRepository.findAll();
        return userList.size();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean changeRoleToSuperUser(SuperUserDTO superUserDTO, int departmentId) {
        boolean isSaved = false;

        User userToAddSuperUserRole = userRepository.findById(superUserDTO.getId()).get();
        userToAddSuperUserRole.setRole(Constants.ROLE_SUPERUSER);
        User tempUser = userRepository.save(userToAddSuperUserRole);
        log.info(" | User " + userToAddSuperUserRole.getEmail() + " promoted to SUPER_USER");

        isSaved = (tempUser.getId() != 0);

        Department departmentToFecthSuperUser = departmentRepository.findById(departmentId).get();
        User userToRemoveSuperUserRole = userRepository.findById(departmentToFecthSuperUser.getSuperUserId().getId()).get();
        userToRemoveSuperUserRole.setRole(Constants.ROLE_USER);
        User tempUser2 = userRepository.save(userToRemoveSuperUserRole);
        log.info(" | User " + userToRemoveSuperUserRole.getEmail() + " new role is ROLE_USER");

        isSaved = (tempUser2.getId() != 0);

        departmentToFecthSuperUser.setSuperUserId(userToAddSuperUserRole);
        Department tempDepartment = departmentRepository.save(departmentToFecthSuperUser);
        log.info(" | Department " + departmentToFecthSuperUser.getName() + " new SUPERUSER is " + userToAddSuperUserRole.getEmail());

        isSaved = (tempDepartment.getId() != 0);

        return isSaved;
    }
}
