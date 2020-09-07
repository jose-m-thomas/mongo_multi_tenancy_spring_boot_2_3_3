package com.josethomas.mongo.multitenant;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Autowired
	private CustomerRepository repository;

	@Bean
	InitializingBean sendDatabase() {
		return () -> {
			try {
				TenantProvider.setCurrentDb("DB1");

				Customer cust=new Customer();
				cust.firstName="customer 1";
				repository.save(cust);

				TenantProvider.setCurrentDb("DB2");

				Customer cust2=new Customer();
				cust2.firstName="customer 2";
				repository.save(cust2);

				TenantProvider.setCurrentDb("DB1");
				Customer cust3=new Customer();
				cust3.firstName="customer 3";
				repository.save(cust3);

				TenantProvider.setCurrentDb("DB2");

				Customer cust4=new Customer();
				cust4.firstName="customer 4";
				repository.save(cust4);

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		};
	}
}
