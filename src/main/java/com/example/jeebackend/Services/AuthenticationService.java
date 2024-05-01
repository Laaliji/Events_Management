package com.example.jeebackend.Services;

import com.example.jeebackend.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;

import java.util.Arrays;
import java.util.Date;

@Stateless
@Local
public class AuthenticationService {

    private static final String SECRET_KEY = "nDG8hN6wHZQwh7JV9dXPyb2tT3vzFafU";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds

    public String generateJwtToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims parseJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = parseJwtToken(token).getExpiration();
        return expirationDate.before(new Date());
    }

    public String getEmailFromToken(String token) {
        return parseJwtToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(parseJwtToken(token).get("userId").toString());
    }

    public String getRoleFromToken(String token) {
        return parseJwtToken(token).get("role").toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAuthorized(String token, String... allowedRoles) {
        if (token != null && validateToken(token)) {
            String role = getRoleFromToken(token);
            return Arrays.asList(allowedRoles).contains(role);
        }
        return false;
    }
}
