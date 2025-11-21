package com.ascendion.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void adminControllerBeanShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.AdminController.class))
                .as("AdminController bean should be registered")
                .isNotEmpty();
    }

    @Test
    void adminServiceAndRepositoryBeansShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.AdminService.class))
                .as("AdminService bean should be registered")
                .isNotEmpty();
    }
}

