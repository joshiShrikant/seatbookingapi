package com.ascendion.demo.service;

import com.ascendion.demo.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
    Role getByName(String name); // throws ResourceNotFoundException when not found
}

