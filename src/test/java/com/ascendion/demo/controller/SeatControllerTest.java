package com.ascendion.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SeatControllerTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void seatControllerBeanShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.SeatController.class))
                .as("SeatController bean should be registered")
                .isNotEmpty();
    }

    @Test
    void seatServiceAndRepositoryBeansShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.SeatService.class))
                .as("SeatService bean should be registered")
                .isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.SeatRepository.class))
                .as("SeatRepository bean should be registered")
                .isNotEmpty();
    }
}

