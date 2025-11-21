package com.ascendion.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RepositoriesPresenceTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    void repositoryBeansShouldBePresent() {
        // assert that repository beans are registered in the context
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.RoleRepository.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.UserRepository.class)).isNotEmpty();
        assertThat(ctx.getBeanNamesForType(com.ascendion.demo.repository.BookingRepository.class)).isNotEmpty();
    }
}

