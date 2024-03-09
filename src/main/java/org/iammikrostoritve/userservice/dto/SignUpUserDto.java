package org.iammikrostoritve.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iammikrostoritve.userservice.model.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
}
