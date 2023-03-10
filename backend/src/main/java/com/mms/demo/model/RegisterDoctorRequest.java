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
public class RegisterDoctorRequest {
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private Long specialityId;
}
