package com.ascendion.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServicesPresenceTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void serviceBeansShouldBePresent() {
        // assert that service beans are registered in the context
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.SeatService.class)).isNotNull();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.UserService.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.BookingService.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.service.RoleService.class)).isNotEmpty();
    }
}

