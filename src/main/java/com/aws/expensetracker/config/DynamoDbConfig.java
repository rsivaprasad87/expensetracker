package com.aws.expensetracker.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;


@Configuration
public class DynamoDbConfig {

    @Bean
    @Profile("local")
    public DynamoDbClient localDynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://host.docker.internal:8000"))
                .region(Region.AP_SOUTH_1)
                .build();
    }

    @Bean
    @Profile("aws")
    public DynamoDbClient awsDynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1) // your AWS region
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}

