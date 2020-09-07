package com.josethomas.mongo.multitenant;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CachedMongoClients {

    @Value("${mongo.default.host}")
    private String host;
    @Value("${mongo.default.port}")
    private String port;

    @Value("${mongo.default.database}")
    private String databaseName;
    @Value("${mongo.default.userName}")
    private String userName;
    @Value("${mongo.default.password}")
    private String password;

    Map<String, Tenant> tenantCache;

    //*Intitialize your Multi tenant Here here.
    @PostConstruct
    @Lazy
    public void initTenant() {
        tenantCache = new HashMap<>();
        // Two Db's, this expapple got two MOngo DB running in local host on different port
        Tenant tenant1 = new Tenant(host, 27017, databaseName, userName, password);
        Tenant tenant2 = new Tenant(host, 27027, databaseName, userName, password);

        tenantCache.put("DB1", tenant1);
        tenantCache.put("DB2", tenant2);
    }

    /**
     * Default Database name
     *
     * @return
     */
    @Bean
    String databaseName() {
        return databaseName;
    }

    /**
     * Default Mongo Connection
     */
    @Bean
    public MongoClient getMongoClient() {
        log.info("Default Mongo Connection ");
        MongoCredential credential = MongoCredential.createCredential(userName, databaseName, password.toCharArray());
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                .credential(credential)
                .build());
        return mongoClient;
    }


    public MongoDatabase getMongoDatabaseForCurrentContext() {
        log.info(">>>"+TenantProvider.getCurrentDb());
        return tenantCache.get(TenantProvider.getCurrentDb()).getClient().
                getDatabase(tenantCache.get(TenantProvider.getCurrentDb()).getDatabase());
    }
}
