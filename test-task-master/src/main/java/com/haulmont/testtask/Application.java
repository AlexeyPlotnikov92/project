package com.haulmont.testtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.Test;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        Test test = new Test();
//        if (!test.loadDriver()) return;
//        if (!test.getConnection()) return;
//
//        test.createTable();
    }
}
