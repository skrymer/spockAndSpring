package com.skrymer.controller

import com.skrymer.model.Person
import com.skrymer.service.PersonService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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

        when: "Creating a person named Sonni aged 42"
        def response = mockMvc.perform(post('/person')
                .contentType(APPLICATION_JSON)
                .content('{"name":"Sonni", "age":32}'))

        then: "delegate creation to PersonService"
        1 * personService.createPerson(
                { Person p -> p.name == 'Sonni' && p.age == 32 }
        )

        and: "respond with 200"
        response
                .andExpect(status().isOk());
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

    def 'delete a person named Sonni'() {
        when: 'deleting sonni'
        def response = mockMvc.perform(delete('/person/sonni'));

        then: 'delegate deletion to personService'
        1 * personService.deletePerson('sonni');

        and: 'respond with status 200'
        response.andExpect(status().isOk());
    }

    def 'create person aged 121'() {
        when: 'creating a person with age 121'
        def response = mockMvc.perform(post('/person')
                .contentType(APPLICATION_JSON)
                .content('{"name":"Sonni", "age":121}')
        );

        then: 'return error message'
        response.andExpect(jsonPath('$[0].field', equalToIgnoringCase('age')));
        response.andExpect(jsonPath('$[0].error', equalToIgnoringCase('must be less than or equal to 120')));

        and: 'respond with status 400'
        response.andExpect(status().isBadRequest());
    }

    def 'create person aged -1'() {
        when: 'creating a person with age -1'
        def response = mockMvc.perform(post('/person')
                .contentType(APPLICATION_JSON)
                .content('{"name":"Sonni", "age":-1}')
        );

        then: 'return error message'
        response.andExpect(jsonPath('$[0].field', equalToIgnoringCase('age')));
        response.andExpect(jsonPath('$[0].error', equalToIgnoringCase('must be greater than or equal to 0')));

        and: 'respond with status 400'
        response.andExpect(status().isBadRequest());
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
