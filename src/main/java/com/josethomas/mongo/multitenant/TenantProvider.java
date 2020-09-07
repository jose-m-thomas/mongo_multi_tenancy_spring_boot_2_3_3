package com.josethomas.mongo.multitenant;

import org.springframework.stereotype.Component;

@Component
public class TenantProvider {
    static ThreadLocal<String> currentDb=new ThreadLocal();
    public static String getCurrentDb(){
        return currentDb.get();
    }
    public static void  setCurrentDb(String dbName){
         currentDb.set(dbName);
    }
}
