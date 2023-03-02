package com.sparta.sbug.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    public String bucketName;
    @Value("${cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;
    @Value("${cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;

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

    public S3Presigner getPreSigner() {
        AwsCredentialsProvider awsCredentialsProvider;
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        awsCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

        Region region = Region.AP_NORTHEAST_2;
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }
}
