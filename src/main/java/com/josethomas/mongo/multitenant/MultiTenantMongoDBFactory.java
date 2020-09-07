package com.josethomas.mongo.multitenant;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Arrays;

@Configuration
@Slf4j
public class MultiTenantMongoDBFactory extends SimpleMongoClientDatabaseFactory {

    @Autowired
    CachedMongoClients cachedMongoClients;
    public MultiTenantMongoDBFactory(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
        log.info("## MultiTenantMongoDBFactory");

    }

    protected MongoDatabase doGetMongoDatabase(String dbName) {
        log.info("## doGetMongoDatabase");
        return cachedMongoClients.getMongoDatabaseForCurrentContext();
        //return getMongoClient().getDatabase(dbName);
    }




}
