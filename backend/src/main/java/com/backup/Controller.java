package com.backup;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

@RestController
public class Controller {

    private final AmazonS3 s3Client;

    // 🔥 Inject S3 client (make sure it's configured)
    public Controller(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    private final String bucketName = "backup-app-storage1"; // 🔴 CHANGE THIS

    // =========================
    // 📤 UPLOAD (MULTI FILE / FOLDER)
    // =========================
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {

                String fileName = file.getOriginalFilename();

                InputStream inputStream = file.getInputStream();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());

                s3Client.putObject(bucketName, fileName, inputStream, metadata);
            }

            return ResponseEntity.ok("Upload successful");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed");
        }
    }

    // =========================
    // 📥 DOWNLOAD (CHECK S3 FIRST)
    // =========================
    @GetMapping("/restore/{filename}")
    public ResponseEntity<?> restore(@PathVariable String filename) {
        try {

            // 🔥 Check file exists in S3
            if (!s3Client.doesObjectExist(bucketName, filename)) {
                return ResponseEntity.status(404).body("File not found in S3");
            }

            S3Object object = s3Client.getObject(bucketName, filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(object.getObjectContent());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Download failed");
        }
    }
}
