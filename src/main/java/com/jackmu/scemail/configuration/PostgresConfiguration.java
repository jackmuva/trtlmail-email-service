package com.jackmu.scemail.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class PostgresConfiguration {
    @Bean
    public DataSource dataSource(){
        final Map<String, String> env = System.getenv();
        DataSourceBuilder builder = DataSourceBuilder.create();

        String url = env.get("postgres_url");
        String username = env.get("pg_username");
        String password = env.get("pg_pw");

        builder.driverClassName("org.postgresql.Driver")
                .url(url)
                .username(username)
                .password(password);
        return builder.build();
    }
}
