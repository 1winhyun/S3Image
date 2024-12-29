package com.example.s3_image.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDTO {
    private String name;
    private String url;

    public static ImageRequestDTO of(String name, String url) {
        return new ImageRequestDTO(name, url);
    }
}
