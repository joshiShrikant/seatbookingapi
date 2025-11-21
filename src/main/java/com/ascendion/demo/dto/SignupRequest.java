package com.ascendion.demo.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String userName;
    private String email;
    private String password;

}
