package com.ascendion.demo.controller;

import com.ascendion.demo.dto.LoginRequest;
import com.ascendion.demo.dto.SignupRequest;
import com.ascendion.demo.entity.User;
import com.ascendion.demo.repository.UserRepository;
import com.ascendion.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;  // ← this removes the error
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest req) {
        return ResponseEntity.ok(userService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = userService.login(req);
        return ResponseEntity.ok(Map.of("token", token));
    }

    // logout endpoint can be implemented here if needed
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutPost(HttpServletRequest request, HttpServletResponse response) {
        performLogout(request, response);
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }

    // also allow GET for convenience (useful for clients where CSRF isn't enforced)
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutGet(HttpServletRequest request, HttpServletResponse response) {
        performLogout(request, response);
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }

    @GetMapping("/deregister")
    public ResponseEntity<?> deRegister(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        // Implementation to delete a user
        return ResponseEntity.ok("User deregister successfully with user Id" + userId);
    }

    private void performLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        } else {
            // fallback: invalidate session if present
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
        }

        // clear JSESSIONID cookie so browser removes session cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // clear security context explicitly
        SecurityContextHolder.clearContext();
    }
}
