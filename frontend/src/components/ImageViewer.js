import React, { useState } from 'react';
import './ImageViewer.css';

const ImageViewer = ({ photos, onDelete, onEdit }) => {
  const [selectedPhoto, setSelectedPhoto] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);

  const openModal = (photo, index) => {
    setSelectedPhoto(photo);
    setCurrentIndex(index);
  };

  const closeModal = () => {
    setSelectedPhoto(null);
  };

  const goToPrevious = () => {
    const newIndex = currentIndex === 0 ? photos.length - 1 : currentIndex - 1;
    setCurrentIndex(newIndex);
    setSelectedPhoto(photos[newIndex]);
  };

  const goToNext = () => {
    const newIndex = currentIndex === photos.length - 1 ? 0 : currentIndex + 1;
    setCurrentIndex(newIndex);
    setSelectedPhoto(photos[newIndex]);
  };

  const handleDelete = (photoId, e) => {
    e.stopPropagation();
    if (onDelete) {
      onDelete(photoId);
      if (selectedPhoto && selectedPhoto.id === photoId) {
        closeModal();
      }
    }
  };

  const handleEdit = (photo, e) => {
    e.stopPropagation();
    if (onEdit) {
      onEdit(photo);
    }
  };

  const handleDownload = (photo, e) => {
    e.stopPropagation();
    const link = document.createElement('a');
    link.href = photo.url;
    link.download = `photo-${photo.id}.jpg`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  if (!photos || photos.length === 0) {
    return <div className="image-viewer-empty">暂无照片</div>;
  }

  return (
    <div className="image-viewer">
      <div className="thumbnails-grid">
        {photos.map((photo, index) => (
          <div key={photo.id} className="thumbnail-wrapper">
            <img
              src={photo.url}
              alt={`照片 ${index + 1}`}
              className="thumbnail"
              onClick={() => openModal(photo, index)}
            />
            <div className="photo-actions">
              <button 
                className="edit-button" 
                onClick={(e) => handleEdit(photo, e)}
                title="编辑"
              >
                ✏️
              </button>
              <button 
                className="download-button" 
                onClick={(e) => handleDownload(photo, e)}
                title="下载"
              >
                ⬇️
              </button>
              <button 
                className="delete-button" 
                onClick={(e) => handleDelete(photo.id, e)}
                title="删除"
              >
                🗑️
              </button>
            </div>
            <div className="photo-info">
              <span className="photo-date">{photo.createdAt}</span>
            </div>
          </div>
        ))}
      </div>

      {selectedPhoto && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <button className="modal-close" onClick={closeModal}>×</button>
            <button className="modal-prev" onClick={goToPrevious}>‹</button>
            <img src={selectedPhoto.url} alt="查看照片" className="modal-image" />
            <button className="modal-next" onClick={goToNext}>›</button>
            <div className="modal-info">
              <span className="modal-date">{selectedPhoto.createdAt}</span>
              <div className="modal-actions">
                <button 
                  className="modal-edit-button" 
                  onClick={(e) => {
                    e.stopPropagation();
                    handleEdit(selectedPhoto, e);
                  }}
                >
                  编辑
                </button>
                <button 
                  className="modal-download-button" 
                  onClick={(e) => {
                    e.stopPropagation();
                    handleDownload(selectedPhoto, e);
                  }}
                >
                  下载
                </button>
                <button 
                  className="modal-delete-button" 
                  onClick={(e) => {
                    e.stopPropagation();
                    handleDelete(selectedPhoto.id, e);
                  }}
                >
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ImageViewer;