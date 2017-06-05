package com.isobar.sample.action.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fabio.goncalves on 5/06/2017.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServerApplication.class, args);
    }
}
