package com.skrymer.config;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class FongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test-db";
    }

    @Override
    public Mongo mongo() {
        return new Fongo(getDatabaseName()).getMongo();
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.skrymer.model";
    }
}
