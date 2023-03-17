package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    @Builder.Default
    private String gender = null;
    @Builder.Default
    private Integer age = null;
    private String email;
    @Builder.Default
    private String phone = null;
    private String password;
    private Role role;
}
