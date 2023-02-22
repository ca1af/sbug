package com.sparta.sbug.aws.controller;

import com.sparta.sbug.aws.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {
    private static final String FILE_NAME = "fileName";

    @Autowired
    FileService fileService;

    @GetMapping
    public ResponseEntity<Object> findByName(HttpServletRequest request, @RequestBody(required = false) Map<String, String> params) {
        final String path = request.getServletPath();
        if (params.containsKey(FILE_NAME))
            return new ResponseEntity<>(fileService.findByName(params.get(FILE_NAME)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveFile(@RequestParam("extension") String extension) {
        return new ResponseEntity<>(fileService.save(extension), HttpStatus.OK);
    }

}
