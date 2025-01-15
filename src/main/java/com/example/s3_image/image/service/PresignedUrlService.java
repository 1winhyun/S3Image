package com.example.s3_image.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.s3_image.image.dto.PresignedUrlResponse;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {
    private final S3Template s3Template;
    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("ap-northeast-2")
    private String region;

    @Transactional
    public PresignedUrlResponse getPreSignedUrl(String prefix, String originalFilename) {
        String fileName = createPath(prefix, originalFilename);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucketName, fileName);
        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        //photoRepository.save(photo);
        return PresignedUrlResponse.from(presignedUrl.toString());
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName).withMethod(HttpMethod.PUT).withExpiration(getPreSignedUrlExpiration());

        //generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createPath(String prefix, String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", prefix, fileId + fileName);
    }

    private String generateFileAccessUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }
}
