package com.ascendion.demo.service.impl;

import com.ascendion.demo.entity.Role;
import com.ascendion.demo.exception.ResourceNotFoundException;
import com.ascendion.demo.repository.RoleRepository;
import com.ascendion.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));
    }
}

