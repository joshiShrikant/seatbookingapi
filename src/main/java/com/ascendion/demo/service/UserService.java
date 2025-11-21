package com.ascendion.demo.service;

import com.ascendion.demo.dto.LoginRequest;
import com.ascendion.demo.dto.SignupRequest;
import com.ascendion.demo.dto.SignupResponse;
import com.ascendion.demo.entity.User;

public interface UserService {
    SignupResponse register(SignupRequest dto);
    String login(LoginRequest dto);
    Long getUserIdFromUserName(String token);
}
