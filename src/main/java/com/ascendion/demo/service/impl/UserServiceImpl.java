package com.ascendion.demo.service.impl;

import com.ascendion.demo.dto.LoginRequest;
import com.ascendion.demo.dto.SignupRequest;
import com.ascendion.demo.dto.SignupResponse;
import com.ascendion.demo.entity.Role;
import com.ascendion.demo.entity.User;
import com.ascendion.demo.exception.BadRequestException;
import com.ascendion.demo.exception.ResourceNotFoundException;
import com.ascendion.demo.repository.RoleRepository;
import com.ascendion.demo.repository.UserRepository;
import com.ascendion.demo.security.JwtUtils;
import com.ascendion.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder, JwtUtils jwtUtils){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public SignupResponse register(SignupRequest dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

//        if (dto.isAdmin()) {
//            roles.add(roleRepo.findByName("ROLE_ADMIN")
//                    .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
//        }

        user.setRoles(Set.of(role));
       User dbUser =  userRepo.save(user);
        SignupResponse savedUser = new SignupResponse();
        savedUser.setId(dbUser.getId());
        savedUser.setUserName(dto.getUserName());
        savedUser.setEmail(dto.getEmail());
        savedUser.setRoles(Set.of(role));
        return savedUser;
    }


    @Override
    public String login(LoginRequest dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        return jwtUtils.generateToken(user.getUserName()); // maybe through error as it is passing full name
    }

    @Override
    public Long getUserIdFromUserName(String userName) {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userName));
        return user.getId();
    }
}
