package com.danielpm1982.spring_security_demo.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Log
public class MyAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getHeader("Authorization") != null) {
            try {
                Authentication userAuthentication = TokenManager.decodeToken(request);
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            } catch (Exception e){
                log.warning("JWT validation failed ! "+e.getCause()+" "+e.getMessage());
                throw new BadCredentialsException("JWT validation failed !");
            }
        }
        // filter chain only proceeds in case there's no token at the request Authorization header or if the token validates successfully
        filterChain.doFilter(request,response);
    }
}

/*
if the JWT token does not validate successfully at the TokenManager class, the thrown Exception there will be caught here and
a manually written response must be sent to the user - as we can't use @ControllerAdvice, @ExceptionHandler or @ResponseEntity
from here. The Controllers won't even be called, as the filter chain flux will be blocked. In this case, instead of writing the
Http error response here, we can delegate this responsibility to a custom JwtAuthenticationEntryPoint class. And for Spring to
redirect the flux there, we just have to throw a BadCredentialsException. The JwtAuthenticationEntryPoint must also be injected
and registered at the SecurityFilterChain at the WebSecurityConfig, along with this custom authentication filter. In such a case,
for the JwtAuthenticationEntryPoint to be called, we must add this filter after the ExceptionTranslationFilter and not before the
UsernamePasswordAuthenticationFilter, as explained at the WebSecurityConfig class.
*/
