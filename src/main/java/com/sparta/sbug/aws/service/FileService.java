package com.sparta.sbug.aws.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {
    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return amazonS3.generatePresignedUrl(s3BucketName, fileName, calendar.getTime(), httpMethod).toString();
    }

    @Async
    public String findByName(String fileName) {
        if (!amazonS3.doesObjectExist(s3BucketName, fileName))
            return "파일을 찾을 수 없습니다.";
        LOG.info("Generating signed URL for file name {}", fileName);
        return generateUrl(fileName, HttpMethod.GET);
    }

    @Async
    public String save(String extension) {
        String fileName = UUID.randomUUID().toString() + extension;
        return generateUrl(fileName, HttpMethod.PUT);
    }
}
