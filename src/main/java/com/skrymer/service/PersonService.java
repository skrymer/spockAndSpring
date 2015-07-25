package com.skrymer.service;

import com.skrymer.model.Person;

public interface PersonService {
    void createPerson(Person person);

    Person getPerson(String name);

    void deletePerson(String name);
}
