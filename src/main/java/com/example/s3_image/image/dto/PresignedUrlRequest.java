package com.example.s3_image.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PresignedUrlRequest {
    private String prefix;
    private String imageName;
}
