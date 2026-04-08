package com.backup;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

@RestController
public class Controller {

    private final String bucketName = "backup-app-storage1";

    private S3Client s3 = S3Client.builder()
            .region(software.amazon.awssdk.regions.Region.AP_SOUTH_1)
            .credentialsProvider(StaticCredentialsProvider.create(
		AwsBasicCredentials.create(
    System.getenv("AWS_ACCESS_KEY"),
    System.getenv("AWS_SECRET_KEY")
)
            .build();

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(file.getOriginalFilename())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        return "Uploaded to S3";
    }

    @GetMapping("/restore/{name}")
    public byte[] download(@PathVariable String name) throws Exception {

        return s3.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(name)
                        .build()
        ).asByteArray();
    }
}
