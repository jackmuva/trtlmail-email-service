package com.jackmu.scemail.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

@Configuration
@Profile("!local-profile")
public class AwsSesConfig {
    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService(){
        final Map<String, String> env = System.getenv();

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        env.get("aws_access_key"),
                                        env.get("aws_secret_key")
                                )
                        )
                )
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    public JavaMailSender javaMailSender(AmazonSimpleEmailService amazonSimpleEmailService){
        return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
    }
}
