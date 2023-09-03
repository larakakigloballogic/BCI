package com.bci.exercise.domain.user;

import com.bci.exercise.application.jwt.JWTUtil;
import com.bci.exercise.domain.error.NotFoundException;
import com.bci.exercise.domain.error.UserException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class UserUseCase implements UserRestService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RequestUser requestUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching user details for {}", username);
        User user = userService.findUserByEmail(username);
        requestUser.setUser(user);
        return user;
    }

    @Override
    public UserDetails getUserDetails(String username, String requestToken) throws UsernameNotFoundException {
        User user = (User) loadUserByUsername(username);
        if(requestToken == null && user.getToken() == null || !requestToken.equals(user.getToken())){
            throw new CredentialsExpiredException("Token diferente al ultimo registrado");
        }
        return user;
    }

    @Override
    public User createUser(User user){
        try {
            User userFinded = userService.findUserByEmail(user.getEmail());
            if(userFinded != null){
                throw new UserException("No se puede crear usuario debido a que ya existe.");
            }
        } catch ( UsernameNotFoundException e){
            log.info("crating user " + user);
            user.setActive(true);
            user.setCreated(LocalDateTime.now());
            user.setLastLogin(LocalDateTime.now());
            user.setToken(jwtUtil.createJWT(user));
//            user.setPassword();encode //TODO: ENCRIPTAR PASS
            return userService.createUser(user);
        }
        return null;
    }

    @Override
    public User login() {
        User user = requestUser.getUser();
        if(user == null) {
            throw new NotFoundException("Usuario inexistente");
        }
        user.setToken(jwtUtil.createJWT(user));
        userService.updateUser(user);
        return user;
    }


}
