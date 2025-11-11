package com.danielpm1982.spring_security_demo.security;
import com.danielpm1982.spring_security_demo.dto.UserDTOInput;
import com.danielpm1982.spring_security_demo.exception.MyUnauthorizedErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import javax.crypto.SecretKey;
import java.time.*;
import java.util.Collections;
import java.util.Date;

@Log
public class TokenManager {
    private static final String ISSUER = "https://danielpm1982.com";
    private static final long EXPIRATION = 60*60*1000;
    private static final String SECRET_KEY = "0123456789012345678901234567890123456789";
    // if the user authenticates successfully (through the Controller login endpoint), this method encodes (serializes) data from
    // issuer and user custom properties into a MyToken record, which is returned, as a JWT token (Json property), to the user, in
    // order that he may use this token (while not expired) at future requests to secured endpoints of this server, at the header
    // (Authorization) of his Http calls. This way, he just logs in once, and, once authenticated. is able to access the secured
    // endpoints only by using the generated token below, without the need of keeping the user state through a session, cookies or
    // other types of local storage. Because REST apps are stateless, there's no way to maintain user state, as in an MVC or frontend
    // app, for instance, we could. Tokens are a great and very common solution for this scenario
    public static MyToken encodeToken(UserDTOInput userDTOInput) {
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        //the following claims props are public data and will be accessible by anyone that decodes the token
        //the private component here is the secret key, without which no one can recreate a valid token for the user
        String jwtToken = Jwts.builder()
                .subject(userDTOInput.getUsername())
                .claim("email", userDTOInput.getEmail())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .issuer(ISSUER)
                .signWith(secretKey)
                .compact();
        return new MyToken(jwtToken);
    }
    // whenever a user tries to access a secured endpoint, this method is called by MyAuthenticationFilter to decode
    // (deserialize) the bearer token from the request Http header (Authorization), validate its claims props and return
    // an Authentication Java Object, that is added to the SecurityContextHolder, by MyAuthenticationFilter, before other
    // SecurityFilterChain filters run, bypassing any further security restrictions and letting the authenticated user
    // access all authorized resources, according to his roles and permissions. Differently from the encode method above
    // (which is called directly from the login Controller endpoint), this decode method is called from behind the scenes,
    // as configured at the WebSecurityConfig class
    public static Authentication decodeToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String jwtToken = header.substring(7);
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = (Claims)jwtParser.parse(jwtToken).getPayload();
            String subject = claims.getSubject();
            String email = (String)claims.get("email");
            Date expiration = claims.getExpiration();
            String issuer = claims.getIssuer();
            //you could do a broader validation of the token content as you wished, including comparing with the current user data at the DB
            if(!subject.isEmpty()&&!email.isEmpty()&&expiration.after(new Date(System.currentTimeMillis()))&&issuer.equals(ISSUER)) {
                return new UsernamePasswordAuthenticationToken("valid", null, Collections.emptyList());
            }
            log.warning("JWT validation failed ! Invalid token: "+jwtToken);
            throw new MyUnauthorizedErrorException("");
        }
        return null;
    }
}

/*
You can decode and validate the JWT token against the generation key at JWT official site: https://www.jwt.io .
It will decode all claim properties used when creating the JWT token and will optionally cross-validate it against the
secret key above, if you want it to.
*/
