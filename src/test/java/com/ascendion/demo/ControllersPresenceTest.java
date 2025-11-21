package com.ascendion.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ControllersPresenceTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void controllerBeansShouldBePresent() {
        // assert that controller beans are registered in the context
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.AdminController.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.SeatController.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.BookingController.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.AuthController.class)).isNotEmpty();
    }
}

