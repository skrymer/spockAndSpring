package com.skrymer.controller;

import com.skrymer.model.Person;
import com.skrymer.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PersonController {
    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping(value = "person", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody @Valid Person person) {
        personService.createPerson(person);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationError> handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        return errors.stream()
                .map(error -> new ValidationError(error.getCode(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private static final class ValidationError {
        private String property;
        private String message;

        public ValidationError(String field, String message) {
            this.property = field;
            this.message = message;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}



