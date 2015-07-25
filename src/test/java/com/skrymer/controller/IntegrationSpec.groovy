package com.skrymer.controller

import com.skrymer.Application
import com.skrymer.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase
import static org.springframework.data.mongodb.core.query.Criteria.where
import static org.springframework.data.mongodb.core.query.Query.query
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

/**
 * Integration specification
 */
@WebIntegrationTest
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Application.class])
class IntegrationSpec extends Specification {

    @Autowired
    WebApplicationContext wac
    @Autowired
    MongoTemplate mongo;

    MockMvc mockMvc

    def setup() {
        this.mockMvc = webAppContextSetup(this.wac)
                .alwaysDo(print())
                .build();
    }

    def 'post'() {
        given: "a person named Sonni"
        def details = '{"name":"Sonni","age":42}';

        when: "creating Sonni"
        def response =
                mockMvc.perform(post('/person')
                        .contentType(APPLICATION_JSON)
                        .content(details)
                );

        then: "Sonni is created"
        mongo.exists(query(where('name').is('Sonni')), Person.class) == true;

        and: "respond with status ok"
        response.andExpect(status().isOk());
    }

    def 'get'() {
        given: "Sonni exists"
        mongo.insert(new Person('Sonni', 42));

        when: "getting person with name Sonni"
        def response = mockMvc.perform(get('/person/Sonni'));

        then: "return Sonni"
        response.andExpect(jsonPath('$.name', equalToIgnoringCase("sonni")))
                .andExpect(status().isOk());
    }

    def 'delete'() {
        given: "Sonni exists"
        mongo.insert(new Person('Sonni', 42));

        when: "deleting Sonni"
        def response = mockMvc.perform(delete('/person/Sonni'));

        then: "Sonni is deleted"
        mongo.exists(query(where('name').is('Sonni')), Person.class) == false;

        and: "respond with status ok"
        response.andExpect(status().isOk());
    }

    def 'update'() {
        given: "Sonni exists"
        mongo.insert(new Person('Sonni', 32));

        when: "updating Sonni's age to 42"
        def response = mockMvc.perform(put('/person')
                .contentType(APPLICATION_JSON)
                .content('{"name":"Sonni", "age":42}'));

        then: "Sonni's age is updated to 42"
        mongo.findOne(query(where('name').is('Sonni')), Person.class).age == 42;

        and: "respond with status ok"
        response.andExpect(status().isOk());
    }

    //TODO errors

    //TODO Validation errors
}