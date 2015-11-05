package com.mmnaseri.personal.worthtrend.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mmnaseri.personal.worthtrend.data.model.IterativeTermination;
import com.mmnaseri.personal.worthtrend.data.model.Termination;
import com.mmnaseri.personal.worthtrend.data.model.TimedTermination;
import com.mmnaseri.personal.worthtrend.io.impl.ConsoleImpl;
import com.mmnaseri.personal.worthtrend.io.impl.FileOutputManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:06)
 */
@Configuration
public class InputOutputConfiguration {

    @Bean
    public Console console() {
        return new ConsoleImpl(System.out, System.in);
    }

    @Bean
    public FileOutputManager fileOutputManager() {
        return new FileOutputManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(Termination.class, new JsonDeserializer<Termination>() {
            @Override
            public Termination deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                final Map map = p.readValueAs(Map.class);
                if (map.containsKey("iterations")) {
                    final IterativeTermination termination = new IterativeTermination();
                    termination.setIterations((Integer) map.get("iterations"));
                    return termination;
                } else {
                    final TimedTermination termination = new TimedTermination();
                    termination.setEndDate(new Date((Long) map.get("endDate")));
                    return termination;
                }
            }
        });
        mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.registerModule(module);
        return mapper;
    }

}

