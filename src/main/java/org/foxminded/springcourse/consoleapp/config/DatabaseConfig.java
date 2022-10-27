package org.foxminded.springcourse.consoleapp.config;

import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.foxminded.springcourse.consoleapp")
public class DatabaseConfig {

    @Bean
    public ConnectionConfig connectionConfig() {
        return new ConnectionConfig("jdbc:postgresql://localhost:5432/school",
                "postgres", "postgres");
    }
}
