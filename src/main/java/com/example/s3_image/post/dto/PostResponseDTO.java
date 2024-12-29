package com.example.s3_image.post.dto;

import com.example.s3_image.image.domain.Image;
import com.example.s3_image.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
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
public class PostResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String content;
    private List<String> imagesUrl;

    public static PostResponseDTO from(Post post){
        List<String>imagesUrl=post.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .content(post.getContent())
                .imagesUrl(imagesUrl)
                .build();
    }
}
