package com.skrymer.controller

import com.skrymer.service.PersonService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Created by skrymer on 31/07/15.
 */
class DeletePersonSpecification extends Specification {
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

    def 'delete a person named Sonni'() {
        when: 'deleting sonni'
        def response = mockMvc.perform(delete('/person/sonni'));

        then: 'delegate deletion to personService'
        1 * personService.deletePerson('sonni');

        and: 'respond with status 200'
        response.andExpect(status().isOk());
    }
}
