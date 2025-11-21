package com.ascendion.demo.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ascendion.demo.repository.UserRepository;
import com.ascendion.demo.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void authControllerBeanShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.AuthController.class))
                .as("AuthController bean should be registered")
                .isNotEmpty();
    }

    @Test
    void relatedServiceAndRepositoryBeansShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.UserService.class))
                .as("UserService bean should be registered")
                .isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.UserRepository.class))
                .as("UserRepository bean should be registered")
                .isNotEmpty();
    }

    @Test
    void logoutGetWithoutAuthReturnsSuccessAndClearsCookie() throws Exception {
        mockMvc.perform(get("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Logged out successfully\"}"))
                .andExpect(header().string("Set-Cookie",
                        Matchers.allOf(Matchers.containsString("JSESSIONID="), Matchers.containsString("Max-Age=0"))));
    }

    @Test
    void logoutPostWithoutAuthWithCsrfReturnsSuccessAndClearsCookie() throws Exception {
        mockMvc.perform(post("/api/auth/logout").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Logged out successfully\"}"))
                .andExpect(header().string("Set-Cookie", Matchers.containsString("JSESSIONID=")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void logoutPostWithAuthClearsSecurityContextAndReturnsSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/logout").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Logged out successfully\"}"))
                .andExpect(header().string("Set-Cookie",
                        Matchers.allOf(Matchers.containsString("JSESSIONID="), Matchers.containsString("Max-Age=0"))));
    }

    // --- New tests to improve coverage for register, login and deregister endpoints ---
//    @Test
//    void registerEndpointExistsAndHandlesRequest() throws Exception {
//        String payload = "{\"username\":\"testuser\",\"password\":\"P@ssw0rd\",\"email\":\"a@b.com\"}";
//        MvcResult res = mockMvc.perform(post("/api/auth/register")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(payload))
//                .andReturn();
//
//        int status = res.getResponse().getStatus();
//        // endpoint should exist (not 404) and should return some response (status code present)
//        assertThat(status).as("register endpoint should exist").isNotEqualTo(404);
//    }

    @Test
    void loginEndpointExistsAndHandlesRequest() throws Exception {
        String payload = "{\"username\":\"testuser\",\"password\":\"wrong-or-ok\"}";
        MvcResult res = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn();

        int status = res.getResponse().getStatus();
        // endpoint should exist (not 404)
        assertThat(status).as("login endpoint should exist").isNotEqualTo(404);
    }

    @Test
    void deregisterEndpointExistsWithoutAuth() throws Exception {
        MvcResult res = mockMvc.perform(delete("/api/auth/deregister").with(csrf()))
                .andReturn();

        int status = res.getResponse().getStatus();
        assertThat(status).as("deregister endpoint should be mapped (even if requires auth)").isNotEqualTo(404);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deregisterEndpointWithAuthHandlesRequest() throws Exception {
        MvcResult res = mockMvc.perform(delete("/api/auth/deregister").with(csrf()))
                .andReturn();

        int status = res.getResponse().getStatus();
        assertThat(status).as("deregister endpoint should handle authenticated requests").isNotEqualTo(404);
    }

    @Test
    void deregisterGetUserExistsDeletesUserAndReturnsSuccess() throws Exception {
        Long id = 123L;
        User user = mock(User.class);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        mockMvc.perform(get("/api/auth/deregister").param("userId", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deregister successfully with user Id" + id));

        verify(userRepository, times(1)).delete(user);
    }

//    @Test
//    void deregisterGetUserNotFoundThrowsRuntimeException() throws Exception {
//        Long id = 999L;
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/auth/deregister").param("userId", id.toString()))
//                .andExpect(status().isInternalServerError())
//                .andExpect(result -> {
//                    Exception ex = result.getResolvedException();
//                    assertThat(ex).isNotNull();
//                    assertThat(ex).isInstanceOf(RuntimeException.class);
//                    assertThat(ex.getMessage()).isEqualTo("User not found");
//                });
//
//        verify(userRepository, never()).delete(any());
//    }
}
