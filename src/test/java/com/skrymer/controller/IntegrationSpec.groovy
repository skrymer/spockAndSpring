package com.skrymer.controller

import com.skrymer.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

/**
 * Integration specification
 */
@WebIntegrationTest
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Application.class])
class IntegrationSpec extends Specification {

    @Autowired
    WebApplicationContext wac
    MockMvc mockMvc

    def setup() {
        this.mockMvc = webAppContextSetup(this.wac).alwaysDo(print()).build();
    }

    def 'create a person'() {
        given: "a person"
        def details = '{"name":"Sonni","age":42}';

        when: "creating a person"
        def response =
                mockMvc.perform(post('/person')
                        .contentType(APPLICATION_JSON)
                        .content(details)
                );

        then: "return status ok"
        response.andExpect(status().isOk());
    }

    def 'get a person with name Sonni'() {
        when: "getting person with name Sonni"
        def response = mockMvc.perform(get('/person/sonni'));

        then: "return Sonni"
        response.andExpect(jsonPath('$.name', equalToIgnoringCase("sonni")))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    def 'delete a person with name Sonni'() {
        when: "deleting Sonni"
        def response = mockMvc.perform(delete('/person/sonni'));

        then: "sonni is deleted"
        response.andExpect(status().isOk());
    }

    def 'update age of person Sonni to 32'() {
        given:
        def update = '{"name":"Sonni", "age":32}';

        when: "updating Sonni"
        def response = mockMvc.perform(put('/person')
                .contentType(APPLICATION_JSON)
                .content(update));

        then: "Sonni is updated"
        response.andExpect(status().isOk());
    }

    //TODO errors
}