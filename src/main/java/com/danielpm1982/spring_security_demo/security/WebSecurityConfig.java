package com.danielpm1982.spring_security_demo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public MyAuthenticationFilter myAuthenticationFilter() {
        return new MyAuthenticationFilter();
    }
    @Bean
    public MyJwtAuthenticationEntryPoint myJwtAuthenticationEntryPoint() {
        return new MyJwtAuthenticationEntryPoint();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("Usernames are checked directly at the login Controller - stateless " +
                    "standalone JWT-only authentication ! Tokens are managed directly at TokenManager and added to the " +
                    "SecurityContextHolder by the custom security filter ! No UserDetailsService or UserDetails needed here !");
        };
    }
    @Bean("myDefaultSecurityFilterChainBean")
    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.GET,"/restricted/**").authenticated()
                                .anyRequest().permitAll())
                .exceptionHandling(e->e.authenticationEntryPoint(myJwtAuthenticationEntryPoint()))
//                .addFilterBefore(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
                .addFilterAfter(myAuthenticationFilter(), ExceptionTranslationFilter.class);
        return http.build();
    }
}

/*
Spring security, by default, and for protection against clickjacking, from same origin and from other sites, sets the response
header of Http responses with "X-Frame-Options: DENY". This avoids any content to be displayed into an <iframe> at your browser.
H2-console, in turn, displays its console page in an <iframe>, and therefore has its content blocked by the browser when Spring
security is set on. For solving that issue, and for same-origin (same domain, protocol, and port) content to be allowed inside
an <iframe> - but not from other origins (which preserves security), you just have to have Spring security to set another header
property instead: "X-Frame-Options: SAMEORIGIN". For that, just add the header config line above. And, in case you've not disable
csrf as a whole, you should do it at least for the path "/h2-console/**". Another thing to check out is if this latter path is
permitted to all, without restrictions. Setting all that, h2-console should work normally.
*/

/*
In the case that we explicitly throw a BadCredentialsException inside our custom authentication filter, in case the token does not
validate successfully at the TokenManager, we gotta change two things here, at the WebSecurityConfig:
-> use the exceptionHandling() method at the HttpSecurity to register the custom MyJwtAuthenticationEntryPoint at the
ExceptionHandlingConfigurer<HttpSecurity>;
-> instead of registering the custom authentication filter before UsernamePasswordAuthenticationFilter, while blocking the rest of the
filter chain, in case of error, one should register it after the ExceptionTranslationFilter, so that the request Exception reaches this
latter filter, which is responsible for managing AuthenticationException and/or AccessDeniedException, and, then, for triggering
AuthenticationEntryPoint and/or AccessDeniedHandler. If the custom authentication filter is added before the UsernamePasswordAuthenticationFilter,
which is even before ExceptionTranslationFilter, this latter filter will never see the Exception and therefore will never use the
authenticationEntryPoint. The response will come from the Tomcat general response system and not from the custom MyJwtAuthenticationEntryPoint,
as we wish.
*/

/*
A better way of configuring the handling of exceptions throughout the filter chain is demonstrated at a later version of this app (see the README.md
file for the available versions' links). This most recommended way creates an impl for the AbstractAuthenticationToken and AuthenticationProvider.
Exceptions are managed only inside the Provider, and outside the custom filter, letting this one cleaner. No exceptions are thrown inside the custom
filter, including BadCredentialsException. At this WebSecurityConfig class, the needed classes are injected and the custom filter may be added right
before the UsernamePasswordAuthenticationFilter, and not only after ExceptionTranslationFilter. Spring will care about all the handling on its own.
*/
