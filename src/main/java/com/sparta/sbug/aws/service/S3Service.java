package com.sparta.sbug.aws.service;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@RequiredArgsConstructor
public class S3Service {


    // 조회
    public String getObjectPreSignedUrl(String bucketName, String keyName, S3Presigner preSigner) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = preSigner.presignGetObject(getObjectPresignRequest);
            return presignedGetObjectRequest.url().toString();
        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    // 생성
    public String putObjectPreSignedUrl(String bucketName, String keyName, S3Presigner preSigner) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType("image/png")
                    .build();

            PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest preSignedRequest = preSigner.presignPutObject(preSignRequest);
            return preSignedRequest.url().toString();
        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    // 쓰레드 이미지 업로드
    public String imageObjectPreSignedUrl(String bucketName, String keyName, S3Presigner preSigner) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType("image/png")
                    .build();

            PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest preSignedRequest = preSigner.presignPutObject(preSignRequest);
            return preSignedRequest.url().toString();
        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    // 조회_2
    public String getImageObjectPreSignedUrl(String bucketName, String keyName, S3Presigner preSigner) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = preSigner.presignGetObject(getObjectPresignRequest);
            return presignedGetObjectRequest.url().toString(); //

        } catch (S3Exception e) {
            e.getStackTrace();

        }
        return null;
    }
}