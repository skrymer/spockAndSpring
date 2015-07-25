package com.skrymer.controller

import com.skrymer.model.Person
import com.skrymer.service.PersonService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Controller specification
 */
class ControllerSpec extends Specification {
    PersonService personService
    PersonController underTest
    MockMvc mockMvc

    def setup() {
        personService = Mock(PersonService);
        underTest = new PersonController(personService);
        mockMvc = MockMvcBuilders.standaloneSetup(underTest)
                .alwaysDo(print())
                .build();
    }

    def 'create a person named Sonni'() {

        when: "Creating a person named Sonni"
        def response = mockMvc.perform(post('/person')
                .contentType(APPLICATION_JSON)
                .content('{"name":"Sonni", "age":42}')
        )

        then: "delegate to person service"
        1 * personService.createPerson(
                { Person p -> p.name == 'Sonni' && p.age == 32 }
        )

        and: "respond with 200"
        response
                .andExpect(status().isOk());
    }

}
