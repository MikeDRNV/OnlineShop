package com.akvelon.dorodnikov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Provides start application.
 */
@SpringBootApplication
public class OnlineShopApplication {

    /**
     * Starts bootstrap and launch a Spring application.
     *
     * @param args Java command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }
}