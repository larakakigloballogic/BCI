package com.bci.exercise.config.security;

import com.bci.exercise.model.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String _BEARER = "Bearer ";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(_BEARER)) {
                String jwtToken = headerAuth.substring(7);

                String username = jwtUtil.parseJWT(jwtToken).getSubject();

                UserDetails userDetails = getUserDetails(username, jwtToken);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("Error authenticating user request : {}", ex.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }

    }

    public UserDetails getUserDetails(String username, String requestToken) throws UsernameNotFoundException {
        User user = (User) userDetailsService.loadUserByUsername(username);
        if(requestToken == null && user.getToken() == null || !requestToken.equals(user.getToken())){
            throw new CredentialsExpiredException("Token diferente al ultimo registrado");
        }
        return user;
    }

}
