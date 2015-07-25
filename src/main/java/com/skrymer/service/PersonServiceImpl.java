package com.skrymer.service;

import com.skrymer.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public void createPerson(Person measurement) {
    }

    @Override
    public Person getPerson(String name) {
        return new Person(name, 42);
    }

    @Override
    public void deletePerson(String name) {
    }
}
