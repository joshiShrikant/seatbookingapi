package com.ascendion.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RoleControllerTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void roleControllerBeanShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.RoleController.class))
                .as("RoleController bean should be registered")
                .isNotEmpty();
    }

    @Test
    void roleServiceAndRepositoryBeansShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.RoleService.class))
                .as("RoleService bean should be registered")
                .isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.RoleRepository.class))
                .as("RoleRepository bean should be registered")
                .isNotEmpty();
    }
}

