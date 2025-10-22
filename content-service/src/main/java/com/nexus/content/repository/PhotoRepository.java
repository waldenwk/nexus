package com.nexus.content.repository;

import com.nexus.content.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByAlbumId(Long albumId);
    void deleteByAlbumId(Long albumId);
}