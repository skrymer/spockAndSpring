package com.skrymer.controller

import com.skrymer.model.Person
import com.skrymer.service.PersonService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Update person specification
 */
class UpdatePersonSpecification extends Specification {
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

    def 'update a person named Sonni'() {
        when: 'updating sonnis age to 42'
        def response = mockMvc.perform(put('/person')
                .contentType(APPLICATION_JSON)
                .content('{"name":"Sonni", "age":42}'));

        then: 'delegate to personService'
        1 * personService.updatePerson(
                { Person p -> p.name == 'Sonni' && p.age == 42 }
        );

        and: 'respond with status 200'
        response.andExpect(status().isOk());
    }
}