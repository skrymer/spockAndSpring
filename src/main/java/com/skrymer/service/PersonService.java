package com.skrymer.service;

import com.skrymer.model.Person;

public interface PersonService {
    public void createPerson(Person person);

    public Person getPerson(String name);

    public void deletePerson(String name);

    public void updatePerson(Person person);
}
