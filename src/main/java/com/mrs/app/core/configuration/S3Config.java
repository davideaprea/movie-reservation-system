package com.mrs.app.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    public S3Client configS3Client(
            @Value("${aws.s3.region}")
            String region,

            @Value("${aws.s3.access-key}")
            String accessKey,

            @Value("${aws.s3.secret-key}")
            String secretKey
    ) {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client
                .builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
