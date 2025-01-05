package dev.sujan.signup_login_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
        @Value("${spring.app.jwt.expiration}")
        private  Long expirationDate;

        @Value("${spring.app.secretKey}")
        private String SECRET_KEY;

        public String extractUsername(String jwtToken) {
            return extractClaim(jwtToken,Claims::getSubject);
        }


        public <T> T extractClaim(
                String token,
                Function<Claims,T> claimResolver
        ){
            final Claims claims = extractAllClaims(token);
            return claimResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


        public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

        private  String generateToken(
                Map<String,Object> claims,
                UserDetails userDetails
        ) {
        return buildToken(claims,userDetails,expirationDate);
    }

        private String buildToken(
                Map<String, Object> claims,
                UserDetails userDetails,
                Long expirationDate) {
        List<String> authorityList= userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
       return Jwts
               .builder()
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis()+expirationDate))
               .setClaims(claims)
               .claim("authorities",authorityList)
               .signWith(getSigningKey())
               .compact();
    }

        private Key getSigningKey() {
          byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
          return Keys.hmacShaKeyFor(keyBytes);
    }


        public boolean isTokenValid(UserDetails userDetails, String jwt) {
            return userDetails.getUsername().equals(extractUsername(jwt)) && !isTokenExpired(jwt);
        }

        private boolean isTokenExpired(String jwt) {
        return extractClaim(jwt,Claims::getExpiration).before(new Date());
    }
}
