package com.example.s3_image.post.controller;

import com.example.s3_image.post.dto.PostRequestDTO;
import com.example.s3_image.post.dto.PostResponseDTO;
import com.example.s3_image.post.service.PostService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/api/post",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostResponseDTO>createPost(@RequestPart PostRequestDTO requestDTO,
                                                     @RequestPart List<MultipartFile>images) throws IOException {
        PostResponseDTO post=postService.createPost(requestDTO,images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(post);
    }

    @GetMapping("/api/post")
    public ResponseEntity<List<PostResponseDTO>>findAllPosts(){
        List<PostResponseDTO> posts=postService.findAllPosts();
        return ResponseEntity.ok()
                .body(posts);
    }
}
