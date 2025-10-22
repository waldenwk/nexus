import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../services/api';
import ImageCrop from '../components/ImageCrop';
import ImageEditor from '../components/ImageEditor';
import ImageViewer from '../components/ImageViewer';
import './Album.css';

const Album = () => {
  const [albumName, setAlbumName] = useState('');
  const [albumDescription, setAlbumDescription] = useState('');
  const [photos, setPhotos] = useState([]);
  const [selectedPhoto, setSelectedPhoto] = useState(null);
  const [showCropModal, setShowCropModal] = useState(false);
  const [showEditorModal, setShowEditorModal] = useState(false);
  const [isCreatingAlbum, setIsCreatingAlbum] = useState(false);
  const [albumCreated, setAlbumCreated] = useState(false);
  const [albumId, setAlbumId] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [editing, setEditing] = useState(false);
  const [error, setError] = useState('');
  const fileInputRef = useRef(null);
  const navigate = useNavigate();

  const handleCreateAlbum = async (e) => {
    e.preventDefault();
    if (!albumName.trim()) {
      setError('请输入相册名称');
      return;
    }

    try {
      setIsCreatingAlbum(true);
      const response = await ApiService.createAlbum(albumName, albumDescription);
      if (response.success) {
        setAlbumId(response.data.id);
        setAlbumCreated(true);
        setError('');
      } else {
        setError('创建相册失败: ' + response.message);
      }
    } catch (err) {
      setError('创建相册时发生错误: ' + err.message);
    } finally {
      setIsCreatingAlbum(false);
    }
  };

  const handlePhotoUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      setError('请选择图片文件');
      return;
    }

    try {
      setUploading(true);
      // 先上传到服务器
      const response = await ApiService.uploadPhoto(file);
      if (response.success) {
        // 显示裁剪模态框
        setSelectedPhoto({
          file: file,
          url: URL.createObjectURL(file),
          uploadUrl: response.data.url
        });
        setShowCropModal(true);
      } else {
        setError('图片上传失败: ' + response.message);
      }
    } catch (err) {
      setError('图片上传时发生错误: ' + err.message);
    } finally {
      setUploading(false);
      // 清空文件输入
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    }
  };

  const handleCropComplete = async (croppedImageUrl) => {
    try {
      // 这里应该将裁剪后的图片上传到服务器
      // 为简化起见，我们直接使用原始上传的图片
      const newPhoto = {
        id: Date.now(),
        url: selectedPhoto.uploadUrl,
        createdAt: new Date().toLocaleString()
      };
      
      setPhotos([...photos, newPhoto]);
      setShowCropModal(false);
      setSelectedPhoto(null);
    } catch (err) {
      setError('保存裁剪图片时发生错误: ' + err.message);
    }
  };

  const handleCropPhoto = async (photoId, croppedImageUrl, cropParams) => {
    try {
      // 在实际应用中，这里应该将裁剪后的图片上传到服务器
      const response = await ApiService.cropPhoto(photoId, croppedImageUrl, cropParams);
      if (response.success) {
        // 更新照片URL和裁剪参数
        const updatedPhotos = photos.map(photo => 
          photo.id === photoId 
            ? { ...photo, url: response.data.url, cropParams } 
            : photo
        );
        
        setPhotos(updatedPhotos);
      } else {
        setError('裁剪图片保存失败: ' + response.message);
      }
    } catch (err) {
      setError('保存裁剪图片时发生错误: ' + err.message);
    }
  };

  const handleEditPhoto = (photo) => {
    setSelectedPhoto(photo);
    setShowEditorModal(true);
  };

  const handleEditComplete = async (editedImageUrl, editParams) => {
    try {
      setEditing(true);
      // 在实际应用中，这里应该将编辑后的图片上传到服务器
      const response = await ApiService.editPhoto(selectedPhoto.id, editedImageUrl, editParams);
      if (response.success) {
        // 更新照片URL和其他编辑参数
        const updatedPhotos = photos.map(photo => 
          photo.id === selectedPhoto.id 
            ? { ...photo, url: response.data.url, editParams } 
            : photo
        );
        
        setPhotos(updatedPhotos);
        setShowEditorModal(false);
        setSelectedPhoto(null);
      } else {
        setError('编辑图片保存失败: ' + response.message);
      }
    } catch (err) {
      setError('保存编辑图片时发生错误: ' + err.message);
    } finally {
      setEditing(false);
    }
  };

  const handleDeletePhoto = (photoId) => {
    setPhotos(photos.filter(photo => photo.id !== photoId));
  };

  const triggerFileSelect = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  if (!albumCreated) {
    return (
      <div className="album-create">
        <h1>创建相册</h1>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleCreateAlbum} className="album-form">
          <div className="form-group">
            <label htmlFor="albumName">相册名称 *</label>
            <input
              type="text"
              id="albumName"
              value={albumName}
              onChange={(e) => setAlbumName(e.target.value)}
              placeholder="输入相册名称"
              className="form-input"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="albumDescription">相册描述</label>
            <textarea
              id="albumDescription"
              value={albumDescription}
              onChange={(e) => setAlbumDescription(e.target.value)}
              placeholder="输入相册描述"
              className="form-textarea"
              rows="3"
            />
          </div>
          
          <button 
            type="submit" 
            className="submit-button" 
            disabled={isCreatingAlbum}
          >
            {isCreatingAlbum ? '创建中...' : '创建相册'}
          </button>
        </form>
      </div>
    );
  }

  return (
    <div className="album-page">
      <div className="album-header">
        <h1>{albumName}</h1>
        <p>{albumDescription}</p>
        <button onClick={triggerFileSelect} className="upload-button" disabled={uploading}>
          {uploading ? '上传中...' : '上传照片'}
        </button>
        <input
          type="file"
          ref={fileInputRef}
          onChange={handlePhotoUpload}
          accept="image/*"
          style={{ display: 'none' }}
        />
      </div>
      
      {error && <div className="error-message">{error}</div>}
      
      <ImageViewer 
        photos={photos} 
        onDelete={handleDeletePhoto}
        onEdit={handleEditPhoto}
        onCrop={handleCropPhoto}
      />
      
      {showCropModal && selectedPhoto && (
        <div className="modal-overlay">
          <div className="modal-content">
            <ImageCrop 
              src={selectedPhoto.url}
              onComplete={handleCropComplete}
              onCancel={() => {
                setShowCropModal(false);
                setSelectedPhoto(null);
              }}
            />
          </div>
        </div>
      )}
      
      {showEditorModal && selectedPhoto && (
        <div className="modal-overlay">
          <div className="modal-content">
            <ImageEditor
              src={selectedPhoto.url}
              onComplete={(editedImageUrl, editParams) => handleEditComplete(editedImageUrl, editParams)}
              onCancel={() => {
                setShowEditorModal(false);
                setSelectedPhoto(null);
              }}
            />
            {editing && <div className="editor-loading">保存中...</div>}
          </div>
        </div>
      )}
    </div>
  );
};

export default Album;