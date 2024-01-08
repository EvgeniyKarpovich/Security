package by.karpovich.security.security;

import by.karpovich.security.domain.Role;
import by.karpovich.security.domain.User;
import by.karpovich.security.exception.AccessDeniedException;
import by.karpovich.security.props.JwtProperties;
import by.karpovich.security.service.impl.UserServiceImpl;
import by.karpovich.security.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserServiceImpl userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolvedRoles(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(getUserIdFromRefreshToken(refreshToken));
        User user = userService.getUserEntityById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setUsername(createAccessToken(userId, user.getEmail(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getEmail()));

        return jwtResponse;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        String email = getEmailFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getEmailFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String getUserIdFromRefreshToken(String refreshToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .get("id")
                .toString();
    }

    private List<String> resolvedRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }
}
