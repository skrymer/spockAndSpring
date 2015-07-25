package com.skrymer.service;

import com.skrymer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Service
public class PersonServiceImpl implements PersonService {

    private MongoTemplate mongo;

    @Autowired
    public PersonServiceImpl(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public Person createPerson(Person person) {
        mongo.insert(person);

        return mongo.findOne(query(where("name").is(person.getName())), Person.class);
    }

    @Override
    public Person getPerson(String name) {
        return mongo.findOne(query(where("name").is(name)), Person.class);
    }

    @Override
    public void deletePerson(String name) {
        mongo.remove(query(where("name").is(name)), Person.class);
    }

    @Override
    public void updatePerson(Person person) {
        Query query = query(where("name").is(person.getName()));
        Update update = update("age", person.getAge());

        mongo.updateFirst(query, update, Person.class);
    }
}
