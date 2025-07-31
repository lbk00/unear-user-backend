package com.unear.userservice.common.internal;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


public class InternalKeyAuthFilter extends OncePerRequestFilter {

    private static final String INTERNAL_KEY_HEADER = "X-INTERNAL-KEY";
    private final InternalKeyValidator internalKeyValidator;

    public InternalKeyAuthFilter(InternalKeyValidator internalKeyValidator) {
        this.internalKeyValidator = internalKeyValidator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String providedKey = request.getHeader(INTERNAL_KEY_HEADER);

        if(!internalKeyValidator.isValid(providedKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid Internal Key");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
