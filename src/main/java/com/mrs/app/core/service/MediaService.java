package com.mrs.app.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class MediaService {
    private final S3Client s3Client;
    private final String bucketName;

    public MediaService(
            S3Client s3Client,

            @Value("${aws.s3.bucket}")
            String bucketName
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public URL generatePresignedUploadUrl(String fileName, String contentType) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        S3Presigner presigner = S3Presigner.create();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(3))
                .putObjectRequest(putRequest)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);

        return presigned.url();
    }
}
