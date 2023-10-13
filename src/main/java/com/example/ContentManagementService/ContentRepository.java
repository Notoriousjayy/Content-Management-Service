package com.example.ContentManagementService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Repository
public class ContentRepository {
    private final S3Client s3Client;
    private final String bucketName;

    public ContentRepository(S3Client s3Client, @Value("${aws.s3.bucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void uploadContent(MultipartFile file, Content content) throws IOException {
        // Generate a unique S3 object key for the content item
        String s3ObjectKey = /* Generate a unique key */;

        // Upload the file to S3
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3ObjectKey)
                .build(), RequestBody.fromBytes(file.getBytes()));

        // Update the content object with the S3 object key
        content.setS3ObjectKey(s3ObjectKey);

        // Store content metadata in your database or repository
        // ...
    }

    public ResponseInputStream<GetObjectResponse> downloadContent(String s3ObjectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3ObjectKey)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

    public List<Content> getAllContent() {
        // Retrieve content metadata from your database or repository
        // ...
        return /* List of content items */;
    }

    // Implement methods for updating and deleting content
}
