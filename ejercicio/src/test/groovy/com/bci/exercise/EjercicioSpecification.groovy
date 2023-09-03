package com.bci.exercise

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.PersistenceContext

@AutoConfigureMockMvc
@SpringBootTest(classes = EjercicioApplication.class)
@TestPropertySource(properties = [
        "spring.main.allow-bean-definition-overriding=true",
        "spring.http.encoding.force=true"
])
@PersistenceContext
@Transactional
class EjercicioSpecification extends Specification {


    @Shared
    def jsonSlurper = new JsonSlurper()

    @Autowired
    MockMvc mockMvc

    protected def GET(String path, String token = null) {
        return this.mockMvc.perform(MockMvcRequestBuilders.get(path)
                .contentType(MediaType.APPLICATION_JSON)
                //.params()
                .headers(getHttpHeader(token))
        ).andDo(MockMvcResultHandlers.print()).andReturn().response
    }

    protected def POST(String path, Object body, String token = null) {
        return this.mockMvc.perform(
                MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JsonBuilder(body).toPrettyString())
                        .headers(getHttpHeader(token))
        ).andDo(MockMvcResultHandlers.print()).andReturn().response
    }

    private static HttpHeaders getHttpHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.setContentType(MediaType.APPLICATION_JSON)
        if(token != null){
            httpHeaders.add("Authorization", "Bearer "+token)
        }
        return httpHeaders
    }

    protected Map getResponse(def response) {
        if (!response.getContentAsString().isEmpty()) {
            return jsonSlurper.parseText(response.contentAsString)
        }
        return null
    }

}
