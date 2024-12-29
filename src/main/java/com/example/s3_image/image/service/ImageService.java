package com.example.s3_image.image.service;

import com.example.s3_image.image.domain.Image;
import com.example.s3_image.image.dto.ImageRequestDTO;
import com.example.s3_image.image.repository.ImageRepository;
import com.example.s3_image.post.domain.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${aws.s3.bucketName}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    private final S3Client s3Client;
    private final ImageRepository imageRepository;

    @Transactional
    public List<Image>save(List<MultipartFile>file, Post post)throws IOException{
        List<ImageRequestDTO>dtoList=uploadImages(file);
        List<Image>images=dtoList.stream()
                .map((ImageRequestDTO dto)->Image.builder()
                        .name(dto.getName())
                        .url(dto.getUrl())
                        .post(post)
                        .build())
                .collect(Collectors.toList());

        return imageRepository.saveAll(images);
    }

    public List<ImageRequestDTO> uploadImages(List<MultipartFile> files) throws IOException{
        List<ImageRequestDTO>images=new ArrayList<>();

        for(MultipartFile file: files){
            String originalName=file.getOriginalFilename();
            String fileName=generateFileName(originalName);
            try{
                PutObjectRequest putObjectRequest=PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build();

                PutObjectResponse response=s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                if(response.sdkHttpResponse().isSuccessful()){
                    images.add(ImageRequestDTO.of(originalName,generateFileUrl(fileName)));
                }
                else{
                    throw new IllegalArgumentException("S3 업로드 실패: " + response.sdkHttpResponse().statusCode());
                }
            }catch (Exception e){
                throw new IllegalArgumentException("S3 업로드 중 예외 발생: " + e.getMessage(), e);
            }
        }

        return images;
    }

    private String generateFileName(String originalFileName){
        String uuid= UUID.randomUUID().toString();
        return uuid+"_"+originalFileName.replaceAll("\\s+","_");
    }

    private String generateFileUrl(String fileName){
        return String.format("https://%s.s3.%s.amazonaws.com/%s",bucket,region,fileName);
    }
}
