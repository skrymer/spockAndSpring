package com.skrymer.controller

import com.skrymer.model.Person
import com.skrymer.service.PersonService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Get person specification
 */
class GetPersonSpecification extends Specification {
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

    def 'get a person named Sonni'() {
        when: 'getting a person named Sonni'
        def response = mockMvc.perform(get('/person/sonni'));

        then: 'delegate to personService'
        1 * personService.getPerson('sonni') >> new Person('Sonni', 32);

        and: 'return sonni'
        response.andExpect(jsonPath('$.name', equalToIgnoringCase('sonni')));

        and: 'respond with status 200'
        response.andExpect(status().isOk());
    }
}
