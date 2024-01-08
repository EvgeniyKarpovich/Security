package by.karpovich.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    public void doFilter(
            @NonNull final ServletRequest request,
            @NonNull final ServletResponse response,
            @NonNull final FilterChain filterChain) {
        String authHeader = ((HttpServletRequest) request)
                .getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }
        try {
            if (authHeader != null
                    && jwtTokenProvider.validateToken(authHeader)) {
                Authentication authentication
                        = jwtTokenProvider.getAuthentication(authHeader);
                if (authentication != null) {
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }
        } catch (Exception ignored) {
        }

        filterChain.doFilter(request, response);
    }
}
