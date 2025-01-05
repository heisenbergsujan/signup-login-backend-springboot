package dev.sujan.signup_login_backend.auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/user")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
            ) throws MessagingException {
                authenticationService.register(request);
                return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginRequest loginRequest
    ){
       String token=authenticationService.login(loginRequest);
       return ResponseEntity.ok(AuthenticationResponse
               .builder()
               .token(token)
               .build());
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(
            @RequestParam String activationCode
    ) throws MessagingException {
        authenticationService.activateAccount(activationCode);
        return ResponseEntity.ok("Account Activated!");
    }
}
