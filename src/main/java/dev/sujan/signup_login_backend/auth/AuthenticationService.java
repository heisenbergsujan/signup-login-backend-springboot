package dev.sujan.signup_login_backend.auth;

import dev.sujan.signup_login_backend.email.EmailService;
import dev.sujan.signup_login_backend.exception.ResourceNotFoundException;
import dev.sujan.signup_login_backend.user.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${spring.email.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        Role role = roleRepository
                .findByName("user")
                .orElseThrow(
                        ()-> new ResourceNotFoundException("user role doesn't exist")
                );

       User user= User
               .builder()
               .firstName(request.getFirstName())
               .lastName(request.getLastName())
               .email(request.getEmail())
               .password(passwordEncoder.encode(request.getPassword()))
               .accountLocked(false)
               .enabled(false)
               .roles(List.of(role))
               .build();

       userRepository.save(user);
       sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String token=generateAndSaveActivationToken(user);
        emailService.sendActivationEmail(
                user.getEmail(),
                user.getFullName(),
                "activate_account",
                activationUrl,
                token,
                "Account Activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken= generateActivationToken(6);
       Token token= Token
                .builder()
                .token(generatedToken)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "123456789";
        StringBuilder buildToken = new StringBuilder();
        SecureRandom random = new SecureRandom();
        int i;
        for(i=0;i<length;i++){
            int randomIndex=random.nextInt(characters.length());
            buildToken.append(characters.charAt(randomIndex));
        }
        return buildToken.toString();
    }
}
