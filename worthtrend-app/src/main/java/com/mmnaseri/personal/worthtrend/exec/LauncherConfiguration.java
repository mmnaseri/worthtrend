package com.mmnaseri.personal.worthtrend.exec;

import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:41)
 */
@Configuration
public class LauncherConfiguration {

    @Bean
    public Thread launcher(Console console) {
        final Thread thread = new Thread(new Launcher(console), "launcher");
        thread.start();
        return thread;
    }

}
