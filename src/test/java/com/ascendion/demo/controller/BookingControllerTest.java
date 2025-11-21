package com.ascendion.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookingControllerTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void bookingControllerBeanShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.controller.BookingController.class))
                .as("BookingController bean should be registered")
                .isNotEmpty();
    }

    @Test
    void bookingServiceAndRepositoryBeansShouldBePresent() {
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.BookingService.class))
                .as("BookingService bean should be registered")
                .isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.BookingRepository.class))
                .as("BookingRepository bean should be registered")
                .isNotEmpty();
    }
}

