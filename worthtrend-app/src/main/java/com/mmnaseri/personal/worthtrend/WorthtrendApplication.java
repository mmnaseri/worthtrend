package com.mmnaseri.personal.worthtrend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Configuration.class))
public class WorthTrendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorthTrendApplication.class, args);
    }

}
