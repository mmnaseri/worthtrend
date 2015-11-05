package com.mmnaseri.personal.worthtrend.io;

import com.mmnaseri.personal.worthtrend.io.impl.ConsoleImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:06)
 */
@Configuration
public class ConsoleConfiguration {

    @Bean
    public static Console console() {
        return new ConsoleImpl(System.out, System.in);
    }

}

