package com.ascendion.demo.config;

import com.ascendion.demo.entity.Role;
import com.ascendion.demo.entity.Seat;
import com.ascendion.demo.entity.Floor;
import com.ascendion.demo.repository.RoleRepository;
import com.ascendion.demo.repository.SeatRepository;
import com.ascendion.demo.repository.FloorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final SeatRepository seatRepository;
    private final FloorRepository floorRepository;

    @Override
    public void run(ApplicationArguments args) {
        createRoleIfMissing("ROLE_USER");
        createRoleIfMissing("ROLE_ADMIN");

        seedSeatsIfMissing();
    }

    private void createRoleIfMissing(String roleName) {
        if (roleRepository == null) return; // defensive
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role r = new Role();
            // ...existing Role fields...
            r.setName(roleName);
            return roleRepository.save(r);
        });
    }

    private void seedSeatsIfMissing() {
        if (seatRepository == null || floorRepository == null) return; // defensive

        if (seatRepository.count() > 0) return; // already seeded

        // ensure there is at least one Floor to satisfy Seat.floor (ManyToOne optional = false)
        Floor defaultFloor = floorRepository.findAll().stream().findFirst().orElseGet(() -> {
            Floor f = new Floor();
            // adjust setter if your Floor entity uses a different field name
            f.setName("Default Floor");
            return floorRepository.save(f);
        });

        // create 15 seats
        for (int i = 1; i <= 15; i++) {
            Seat s = new Seat();
            s.setFloor(defaultFloor);
            s.setSeatNumber("S" + i);
//            s.setxCoordinate(i);
//            s.setyCoordinate(1);
            s.setActive(true);
            seatRepository.save(s);
        }
    }
}
