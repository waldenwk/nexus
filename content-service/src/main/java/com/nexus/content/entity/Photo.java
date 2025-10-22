package com.nexus.content.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "album_id", nullable = false)
    private Long albumId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String url;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // 图片编辑参数
    @Column(name = "brightness")
    private Integer brightness;
    
    @Column(name = "contrast")
    private Integer contrast;
    
    @Column(name = "saturation")
    private Integer saturation;
    
    @Column(name = "rotation")
    private Integer rotation;
    
    @Column(name = "filter")
    private String filter;
    
    // 裁剪参数
    @Column(name = "crop_x")
    private Integer cropX;
    
    @Column(name = "crop_y")
    private Integer cropY;
    
    @Column(name = "crop_width")
    private Integer cropWidth;
    
    @Column(name = "crop_height")
    private Integer cropHeight;
    
    // Constructors
    public Photo() {}
    
    public Photo(Long albumId, Long userId, String url) {
        this.albumId = albumId;
        this.userId = userId;
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getAlbumId() {
        return albumId;
    }
    
    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getBrightness() {
        return brightness;
    }
    
    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }
    
    public Integer getContrast() {
        return contrast;
    }
    
    public void setContrast(Integer contrast) {
        this.contrast = contrast;
    }
    
    public Integer getSaturation() {
        return saturation;
    }
    
    public void setSaturation(Integer saturation) {
        this.saturation = saturation;
    }
    
    public Integer getRotation() {
        return rotation;
    }
    
    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
    
    public String getFilter() {
        return filter;
    }
    
    public void setFilter(String filter) {
        this.filter = filter;
    }
    
    public Integer getCropX() {
        return cropX;
    }
    
    public void setCropX(Integer cropX) {
        this.cropX = cropX;
    }
    
    public Integer getCropY() {
        return cropY;
    }
    
    public void setCropY(Integer cropY) {
        this.cropY = cropY;
    }
    
    public Integer getCropWidth() {
        return cropWidth;
    }
    
    public void setCropWidth(Integer cropWidth) {
        this.cropWidth = cropWidth;
    }
    
    public Integer getCropHeight() {
        return cropHeight;
    }
    
    public void setCropHeight(Integer cropHeight) {
        this.cropHeight = cropHeight;
    }
}