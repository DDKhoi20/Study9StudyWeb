package com.example.Study9StudyWeb.controller;

import com.example.Study9StudyWeb.entity.AccountEntity;
import com.example.Study9StudyWeb.entity.RoleEntity;
import com.example.Study9StudyWeb.enums.Role;
import com.example.Study9StudyWeb.repository.AccountRepository;
import com.example.Study9StudyWeb.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SecurityController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

//    @PostConstruct
//    public void init() {
//        createRoleDefault();
//        createAccountDefault();
//    }

    private void createRoleDefault() {
        List<RoleEntity> roleEntityList = roleRepository.findAll();
        if (roleEntityList.isEmpty()) {
            // insert role into database
            List<Role> roleList = new ArrayList<>();
            roleList.add(Role.ROLE_ADMIN);
            roleList.add(Role.ROLE_USER);
            for (Role role : roleList) {
                RoleEntity entity = new RoleEntity();
                entity.setRole(role);
                roleRepository.save(entity);
            }
        }
    }

    private boolean createAccountDefault() {
        List<AccountEntity> accountEntityList = new ArrayList<>();
        accountRepository.findAll().forEach(accountEntityList::add);
        if (accountEntityList.isEmpty()) {
            createRoleDefault(); // Ensure roles are created first
            List<RoleEntity> roleEntityList = roleRepository.findAll();
            Set<RoleEntity> roleEntitySet = new LinkedHashSet<>(roleEntityList);

            if (!roleEntitySet.isEmpty()) {
                AccountEntity accountEntity = new AccountEntity();
                accountEntity.setEmail("admin@gmail.com");
                accountEntity.setLastName("Admin");
                accountEntity.setFirstName("Admin");
                // Sử dụng PasswordEncoder để mã hóa mật khẩu
                accountEntity.setPassword(passwordEncoder.encode("123456"));
                accountEntity.setUserRoles(roleEntitySet);
                accountRepository.save(accountEntity);
                return true;
            }
        }
        return false;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/home")
    public String home(){
        return "hello";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/registerbutton")
    public String registerAccount(@ModelAttribute("email") String email,
                                  @ModelAttribute("password") String password,
                                  @ModelAttribute("againpassword") String againpassword,
                                  @ModelAttribute("firstName") String firstName,
                                  @ModelAttribute("lastName") String lastName,
                                  HttpSession httpSession,
                                  Model model) {
        if (accountRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "This email is already registered!");
            return "/register";
        }
        if (!password.equals(againpassword)) {
            model.addAttribute("error", "Passwords do not match. Please try again!");
            return "/register";
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail(email);
        accountEntity.setPassword(passwordEncoder.encode(password));
        accountEntity.setFirstName(firstName);
        accountEntity.setLastName(lastName);
        accountRepository.save(accountEntity);

        return "/login";
    }
}
