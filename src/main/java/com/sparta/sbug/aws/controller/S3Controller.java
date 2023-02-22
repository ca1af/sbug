package com.sparta.sbug.aws.controller;

import com.sparta.sbug.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;


}
