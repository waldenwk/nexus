package com.nexus.content.controller;

import com.nexus.content.entity.Post;
import com.nexus.content.service.PostService;
import com.nexus.content.service.PostEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PostEventListener postEventListener;
    
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        Post savedPost = postService.savePost(post);
        // 发布事件，将内容推送到相关用户的feed中
        postEventListener.onPostCreated(savedPost);
        return savedPost;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Post post = postService.getPostById(id);
        if (post != null) {
            post.setContent(postDetails.getContent());
            post.setType(postDetails.getType());
            post.setUpdatedAt(java.time.LocalDateTime.now());
            
            Post updatedPost = postService.savePost(post);
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            postService.deletePost(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}