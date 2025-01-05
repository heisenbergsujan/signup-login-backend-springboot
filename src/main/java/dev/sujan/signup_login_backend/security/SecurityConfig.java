package dev.sujan.signup_login_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
               .cors(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(
                       httpReq->httpReq
                               .requestMatchers("auth/user/login","auth/user/register","auth/user/activate-account")
                               .permitAll()
                               .anyRequest()
                               .authenticated()
               )
               .sessionManagement(session->
                       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();
   }
}
