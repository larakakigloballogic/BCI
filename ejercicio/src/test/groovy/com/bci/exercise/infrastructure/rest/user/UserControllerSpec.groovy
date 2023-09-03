package com.bci.exercise.infrastructure.rest.user

import com.bci.exercise.EjercicioSpecification

class UserControllerSpec extends EjercicioSpecification {

    def "Prueba de parametros en singUp - caso: #caso"() {
        given:
        def userRequestDTO = new UserRequestDTO(name,email,password,Set.of())
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
    }

    def "intento de singUp con usuario existente"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'cc@cc.com', 'password', Set.of())
        when:
        def response= POST("/sign-up", userRequestDTO)
        then:
        response.status == 409
        def body = getResponse(response)
        body.error[0].detail == 'No se puede crear usuario debido a que ya existe.'
    }

    def "singUp exitoso"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'a@b.com', 'password', Set.of())
        when:
        def response= POST("/sign-up", userRequestDTO)
        then:
        response.status == 201
        def body = getResponse(response)
    }

    def "Login con usuario no registrado"() {
        given:
        def token = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJhN2Y4ODFkMS0wZGQyLTRmNWQtODJmMS01OTA5MjI1YjM3OTciLCJzdWIiOiJqdWxpbzJAbWFpbC5jb20iLCJpc3MiOiJBcmFrYWtpIExlb25hcmRvIiwiYXVkIjoiYXVkaWVuY2UiLCJpYXQiOjE2OTM3NzAwMzEsImV4cCI6MTY5Mzc3MzYzMSwiTmFtZSI6Ikp1bGlvIEdvbnphbGV6IiwiRW1haWwiOiJqdWxpbzJAbWFpbC5jb20ifQ.tqwPIm_dbufCRSQyDx7_BETHrdBTq4bKllcg-BVU7C8G13oBChYmsSYzIBTtfighfK0uwWhking6Ikm7N0Ys3w'
        when:
        def response= GET("/login", token)
        then:
        response.status == 401
    }

    def "singUp y login exitoso"() {
        given:
        def userRequestDTO = new UserRequestDTO('name', 'a@b.com', 'password', Set.of())
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
        loginBody.password == 'pass'
        org.springframework.util.CollectionUtils.isEmpty(loginBody.phones)
    }

}