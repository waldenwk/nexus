package com.nexus.content.service;

import com.nexus.content.entity.Album;
import com.nexus.content.entity.Photo;
import com.nexus.content.repository.AlbumRepository;
import com.nexus.content.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private PhotoRepository photoRepository;
    
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }
    
    public Album getAlbumById(Long id) {
        return albumRepository.findById(id).orElse(null);
    }
    
    public List<Album> getAlbumsByUserId(Long userId) {
        return albumRepository.findByUserId(userId);
    }
    
    public Album updateAlbum(Album album) {
        return albumRepository.save(album);
    }
    
    public void deleteAlbum(Long id) {
        // 先删除相册中的所有照片
        photoRepository.deleteByAlbumId(id);
        // 再删除相册
        albumRepository.deleteById(id);
    }
    
    public Photo addPhotoToAlbum(Photo photo) {
        return photoRepository.save(photo);
    }
    
    public List<Photo> getPhotosByAlbumId(Long albumId) {
        return photoRepository.findByAlbumId(albumId);
    }
    
    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }
}