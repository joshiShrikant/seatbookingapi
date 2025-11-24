package com.ascendion.demo.dto;

import com.ascendion.demo.entity.Role;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Long id;
    private Set<Role> roles = new HashSet<>();

}
