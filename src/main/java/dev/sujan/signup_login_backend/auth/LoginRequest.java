package dev.sujan.signup_login_backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginRequest {
    @NotEmpty(message = "email cannot be empty")
    @Email(message = "email must be a valid email address")
    private String email;
    @NotEmpty(message = "password cannot be empty")
    @Size(min = 8,message = "Password must be at least 8 characters long")
    private String password;
}
