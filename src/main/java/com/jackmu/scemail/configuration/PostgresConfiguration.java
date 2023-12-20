package com.jackmu.scemail.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class PostgresConfiguration {
    @Bean
    public DataSource dataSource(){
        final Map<String, String> env = System.getenv();

        String url = env.get("postgres_url");
        String username = env.get("pg_username");
        String password = env.get("pg_pw");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
