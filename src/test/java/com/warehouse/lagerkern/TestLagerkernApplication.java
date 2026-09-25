package com.warehouse.lagerkern;

import org.springframework.boot.SpringApplication;

public class TestLagerkernApplication {

    public static void main(String[] args) {
        SpringApplication.from(LagerkernApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
