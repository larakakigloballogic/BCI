package com.bci.exercise.controller

import com.bci.exercise.EjercicioSpecification
import com.bci.exercise.dto.UserRequestDTO

class UserControllerSpec extends EjercicioSpecification {

    def "Prueba de parametros en singUp"() {
        given:
        def userRequestDTO = new UserRequestDTO(name,email,password,new HashSet<>())
        when:
        def response= POST("/sign-up", userRequestDTO)
        then:
        response.status == 400
        response.getContentAsString().contains(caso)
        where:
        name    | email     | password          | caso
        'name'  | null      | 'pass'            | 'Field error in object \'userRequestDTO\' on field \'email\''
        null    | 'a@b.com' | null              | 'Field error in object \'userRequestDTO\' on field \'password\''
        'name'  | 'a@b.com' | 'Aaaaaaa'         | 'size must be between 8 and 12'
        'name'  | 'a@b.com' | 'Aaaaaaaaaaaaa'   | 'size must be between 8 and 12'
        'name'  | 'a@b.com' | 'a2a2fGfdAdAA'    | 'Field error in object \'userRequestDTO\' on field \'password\''
        'name'  | 'a@b.com' | 'abasfGfdfdfa'    | 'Field error in object \'userRequestDTO\' on field \'password\''
        'name'  | 'a@b.com' | 'a2a2f2f2fdaA'    | 'Field error in object \'userRequestDTO\' on field \'password\''
    }

    def "intento de singUp con usuario existente"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'cc@cc.com', 'A2asffdfdf4', new HashSet<>())
        when:
        def response= POST("/sign-up", userRequestDTO)
        then:
        response.status == 409
        def body = getResponse(response)
        body.error[0].detail == 'No se puede crear usuario debido a que ya existe.'
    }

    def "singUp exitoso"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'a@b.com', 'a2asfGfdfd4s', new HashSet<>())
        when:
        def response= POST("/sign-up", userRequestDTO)
        then:
        response.status == 201
        def body = getResponse(response)
    }

    def "Login con token expirado"() {
        given:
        def token = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJhN2Y4ODFkMS0wZGQyLTRmNWQtODJmMS01OTA5MjI1YjM3OTciLCJzdWIiOiJqdWxpbzJAbWFpbC5jb20iLCJpc3MiOiJBcmFrYWtpIExlb25hcmRvIiwiYXVkIjoiYXVkaWVuY2UiLCJpYXQiOi0yMTk0MDU3MTAxLCJleHAiOi0yMTk0MDU3MTAxLCJOYW1lIjoiSnVsaW8gR29uemFsZXoiLCJFbWFpbCI6Imp1bGlvMkBtYWlsLmNvbSJ9.jG9GNbukKH53sE5miX4aN9fPRycvw6D9kcCsjdtV5U6weRY67Bdf3BMihzXpk7ohUWS1gDW--2R8eQVz1DSwPg'
        when:
        def response= GET("/login", token)
        then:
        response.status == 403
    }

    def "Login con usuario no registrado"() {
        given:
        def token = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJhN2Y4ODFkMS0wZGQyLTRmNWQtODJmMS01OTA5MjI1YjM3OTciLCJzdWIiOiJqdWxpbzJAbWFpbC5jb20iLCJpc3MiOiJBcmFrYWtpIExlb25hcmRvIiwiYXVkIjoiYXVkaWVuY2UiLCJpYXQiOjMzMjc1OTg5NDkxLCJleHAiOjMzMjc1OTg5NDkxLCJOYW1lIjoiSnVsaW8gR29uemFsZXoiLCJFbWFpbCI6Imp1bGlvMkBtYWlsLmNvbSJ9.FH0RS0WQB-_lQoRsdJbm0AuhjBXly3kUI1krvbAA5DWF-66_CxiHGAlfdRIbZyWmn2kagWSBjIftuXqimlTfEQ'
        when:
        def response= GET("/login", token)
        then:
        response.status == 401
    }

    def "singUp y login exitoso"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'a@b.com', 'a2asfGfdfdf4', new HashSet<>())
        when:
        def response= POST("/sign-up", userRequestDTO)
        then:
        response.status == 201
        def body = getResponse(response)
        body.id != null
        body.created != null
        body.lastLogin != null
        body.token != null
        and:
        def loginResponse= GET("/login", body.token)
        and:
        loginResponse.status == 200
        def loginBody = getResponse(loginResponse)
        loginBody.name == 'name'
        loginBody.email == 'a@b.com'
        loginBody.password != null
        org.springframework.util.CollectionUtils.isEmpty(loginBody.phones)
    }

}