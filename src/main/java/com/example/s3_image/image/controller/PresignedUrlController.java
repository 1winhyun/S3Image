package com.example.s3_image.image.controller;

import com.example.s3_image.image.dto.PresignedUrlRequest;
import com.example.s3_image.image.dto.PresignedUrlResponse;
import com.example.s3_image.image.service.PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/image")
@RestController
@RequiredArgsConstructor
public class PresignedUrlController {
    private final PresignedUrlService presignedUrlService;

    @PostMapping("/presignedurl")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(@RequestBody PresignedUrlRequest presignedUrlRequest){
        PresignedUrlResponse presignedUrlResponse=presignedUrlService.getPreSignedUrl(
                presignedUrlRequest.getImageName()
        );
        return ResponseEntity.ok(presignedUrlResponse);
    }
}
