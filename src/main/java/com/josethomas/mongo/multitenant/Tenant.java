package com.josethomas.mongo.multitenant;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
public class Tenant {
    String host;
    int port;
    String userName;
    String password;
    String database;
    MongoClient client;

    public Tenant(String host, int port, String databaseName, String userName, String password) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.database = databaseName;
        createClient();
    }

    private void createClient() {
        MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
        client = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress(host, port))))
                .credential(credential)
                .build());
    }
}
