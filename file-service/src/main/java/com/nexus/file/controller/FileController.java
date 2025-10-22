package com.nexus.file.controller;

import com.nexus.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.saveFileAsync(file)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    return ResponseEntity.status(500).body("File upload failed: " + throwable.getMessage());
                });
    }
    
    @PostMapping("/upload/high-concurrency")
    public CompletableFuture<ResponseEntity<String>> uploadFileHighConcurrency(@RequestParam("file") MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = fileService.saveFileHighConcurrency(file);
                return ResponseEntity.ok(url);
            } catch (IOException e) {
                return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
            }
        }).exceptionally(throwable -> {
            return ResponseEntity.status(500).body("File upload failed: " + throwable.getMessage());
        });
    }
    
    @PostMapping("/upload/batch")
    public CompletableFuture<ResponseEntity<String[]>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return fileService.saveFilesAsync(files)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    return ResponseEntity.status(500).body(new String[]{"Batch file upload failed: " + throwable.getMessage()});
                });
    }
    
    @PostMapping("/upload/batch/high-concurrency")
    public CompletableFuture<ResponseEntity<String[]>> uploadFilesHighConcurrency(@RequestParam("files") MultipartFile[] files) {
        return fileService.saveFilesHighConcurrencyAsync(files)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    return ResponseEntity.status(500).body(new String[]{"Batch file upload failed: " + throwable.getMessage()});
                });
    }
    
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileData = fileService.getFile(fileName);
            return ResponseEntity.ok(fileData);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}