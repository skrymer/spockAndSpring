package com.skrymer.controller

import com.skrymer.service.PersonService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Controller specification
 */
class ControllerSpec extends Specification {
    PersonService measurementService
    PersonController underTest
    MockMvc mockMvc

    def setup() {
        measurementService = Mock(PersonService)
        underTest = new PersonController(measurementService)
        mockMvc = MockMvcBuilders.standaloneSetup(underTest).build()
    }

    def 'create a weight measurement'() {
        def value = '100'
        def tag = 'weight'
        def dateTime = '2015-07-05T22:16:18'

        when:
        def response = mockMvc.perform(post('/measurement')
                .contentType(APPLICATION_JSON)
                .content("{\"value\": \"$value\", \"tag\": \"$tag\", \"dateTime\": \"$dateTime\"}")
        )

        then:
//        1 * measurementService.createMeasurement(
//                { Measurement m -> m.value == value && m.tag == tag && m.dateTime == LocalDateTime.parse(dateTime) }
//        )

        response
                .andExpect(status().isOk());
    }

}
