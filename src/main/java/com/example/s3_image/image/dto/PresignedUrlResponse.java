package com.example.s3_image.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PresignedUrlResponse {
    private String preSignedUrl;

    public static PresignedUrlResponse from(String url){
        PresignedUrlResponse response=new PresignedUrlResponse();
        response.setPreSignedUrl(url);
        return response;
    }
}
