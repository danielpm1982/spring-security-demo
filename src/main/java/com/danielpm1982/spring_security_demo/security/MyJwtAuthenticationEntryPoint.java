package com.danielpm1982.spring_security_demo.security;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

public class MyJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String invalidToken = "";
        if(request.getHeader("Authorization")!=null && !request.getHeader("Authorization").isEmpty())
            invalidToken = " Invalid token: "+request.getHeader("Authorization").replace("Bearer ","");
        response.getWriter().write(
                "{ \"Error\": \"Invalid or expired token ! Please log in again for getting a new valid token and retry " +
                        "accessing this resource with it ! "+authException.getMessage()+invalidToken+"\" }"
        );
    }
}

/*
This class is useful to model the Http response, in case of token validation error, only if you're manually throwing
BadCredentialsException from your custom filter and adding the filter before the ExceptionTranslationFilter.class, at
the filter chain. If you're writing the Http response directly from the filter or if you're using the recommended way
which is to create a custom impl for the AbstractAuthenticationToken and the AuthenticationProvider, and use those inside
your filter without explicitly throwing exceptions, then this class should not be needed.
*/
