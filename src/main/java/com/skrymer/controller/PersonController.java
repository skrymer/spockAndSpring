package com.skrymer.controller;

import com.skrymer.model.Person;
import com.skrymer.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {
    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping(value = "person", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Person create(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @RequestMapping(value = "person/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Person get(@PathVariable String name) {
        return personService.getPerson(name);
    }

    @RequestMapping(value = "person", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Person person) {
        personService.updatePerson(person);
    }

    @RequestMapping(value = "person/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String name) {
        personService.deletePerson(name);
    }
}

