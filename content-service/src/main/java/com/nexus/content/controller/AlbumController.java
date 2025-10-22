package com.nexus.content.controller;

import com.nexus.content.entity.Album;
import com.nexus.content.entity.Photo;
import com.nexus.content.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    
    @Autowired
    private AlbumService albumService;
    
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Album createdAlbum = albumService.createAlbum(album);
        return ResponseEntity.ok(createdAlbum);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        Album album = albumService.getAlbumById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public List<Album> getAlbumsByUserId(@PathVariable Long userId) {
        return albumService.getAlbumsByUserId(userId);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album albumDetails) {
        Album album = albumService.getAlbumById(id);
        if (album != null) {
            album.setName(albumDetails.getName());
            album.setDescription(albumDetails.getDescription());
            album.setUpdatedAt(java.time.LocalDateTime.now());
            
            Album updatedAlbum = albumService.updateAlbum(album);
            return ResponseEntity.ok(updatedAlbum);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        Album album = albumService.getAlbumById(id);
        if (album != null) {
            albumService.deleteAlbum(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{albumId}/photos")
    public ResponseEntity<Photo> addPhotoToAlbum(@PathVariable Long albumId, @RequestBody Photo photo) {
        photo.setAlbumId(albumId);
        Photo createdPhoto = albumService.addPhotoToAlbum(photo);
        return ResponseEntity.ok(createdPhoto);
    }
    
    @GetMapping("/{albumId}/photos")
    public List<Photo> getPhotosByAlbumId(@PathVariable Long albumId) {
        return albumService.getPhotosByAlbumId(albumId);
    }
    
    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        albumService.deletePhoto(photoId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/photos/{photoId}")
    public ResponseEntity<Photo> updatePhoto(@PathVariable Long photoId, @RequestBody Photo photoDetails) {
        Photo photo = albumService.getPhotoById(photoId);
        if (photo != null) {
            photo.setUrl(photoDetails.getUrl());
            photo.setBrightness(photoDetails.getBrightness());
            photo.setContrast(photoDetails.getContrast());
            photo.setSaturation(photoDetails.getSaturation());
            photo.setRotation(photoDetails.getRotation());
            photo.setFilter(photoDetails.getFilter());
            photo.setCropX(photoDetails.getCropX());
            photo.setCropY(photoDetails.getCropY());
            photo.setCropWidth(photoDetails.getCropWidth());
            photo.setCropHeight(photoDetails.getCropHeight());
            
            Photo updatedPhoto = albumService.updatePhoto(photo);
            return ResponseEntity.ok(updatedPhoto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}