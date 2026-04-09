package com.backup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

    @Bean
    public AmazonS3 amazonS3() {

        return AmazonS3ClientBuilder.standard()
                .withRegion("ap-south-1")
                .build(); // 🔥 Automatically uses ENV variables
    }
}
