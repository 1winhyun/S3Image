package com.example.s3_image.post.service;

import com.example.s3_image.image.domain.Image;
import com.example.s3_image.image.service.ImageService;
import com.example.s3_image.post.domain.Post;
import com.example.s3_image.post.dto.PostRequestDTO;
import com.example.s3_image.post.dto.PostResponseDTO;
import com.example.s3_image.post.repository.PostRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;

    @Transactional
    public PostResponseDTO createPost(PostRequestDTO requestDTO, List<MultipartFile>images) throws IOException {
        Post post=Post.builder()
                .title(requestDTO.getTitle())
                .author(requestDTO.getAuthor())
                .content(requestDTO.getContent())
                .build();
        postRepository.save(post);

        if(images!=null&&!images.isEmpty()){
            List<Image> image= imageService.save(images,post);
            post.addImage(image);
        }
        return PostResponseDTO.from(post);
    }

    public List<PostResponseDTO>findAllPosts(){
        List<Post>posts=postRepository.findAll()
                .stream()
                .collect(Collectors.toList());

        return posts.stream()
                .map(PostResponseDTO::from)
                .collect(Collectors.toList());
    }
}
