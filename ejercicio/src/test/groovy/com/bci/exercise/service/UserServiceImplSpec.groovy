package com.bci.exercise.service

import com.bci.exercise.EjercicioSpecification
import com.bci.exercise.config.security.JWTUtil
import com.bci.exercise.dto.UserRequestDTO
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImplSpec extends EjercicioSpecification{

    @Autowired
    private UserServiceImpl userService

    @SpringBean
    private JWTUtil jwtUtil = Spy()

    def "createUserEntity prueba de mock"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'cc@cc.com', 'A2asffdfdf4', new HashSet<>())
        jwtUtil.createJWT(_,_,_) >> "jwt"
        when:
        def response= userService.createUserEntity(userRequestDTO)
        then:
        response.token == "jwt"
    }

}
