package com.bookmyevent.dto.response;

import com.bookmyevent.enums.UserRole;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private String fullName;
    private UserRole role;
    private String message;
}