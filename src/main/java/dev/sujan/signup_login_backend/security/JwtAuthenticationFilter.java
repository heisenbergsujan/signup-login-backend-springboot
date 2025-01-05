package dev.sujan.signup_login_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtService jwtService;
    private  final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL());
        System.out.println(request.getContextPath());
        System.out.println(request.getServletPath());
        Set<String> excludedPaths=Set.of("auth/user/login","auth/user/register");
        if(excludedPaths.contains(request.getRequestURI())){
            doFilter(request,response,filterChain);
            return;
        }
        final String authHeader= request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String email;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            doFilter(request,response,filterChain);
            return;
        }
        jwt=authHeader.substring(8);
        email=jwtService.extractUsername(jwt);
        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if(jwtService.isTokenValid(userDetails,jwt)){
                UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
doFilter(request,response,filterChain);
    }
}
