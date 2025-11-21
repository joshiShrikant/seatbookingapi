package com.ascendion.demo.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String userName;
    private String email;
    private String password;

}
