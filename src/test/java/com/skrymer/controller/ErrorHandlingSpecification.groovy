package com.skrymer.controller

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
 * Error handling specification
 */
class ErrorHandlingSpecification extends Specification {
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

    def 'something explodes'() {
        when: 'something explodes'
        def response = mockMvc.perform(get('/person/sonni'));

        then: 'return the exception message'
        1 * personService.getPerson('sonni') >> { throw new RuntimeException('Something exploded') };
        response.andExpect(jsonPath('$.message', equalToIgnoringCase('Something exploded')));

        and: 'respond with status 500'
        response.andExpect(status().isInternalServerError());
    }
}
