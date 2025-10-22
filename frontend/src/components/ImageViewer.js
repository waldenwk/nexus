import React, { useState } from 'react';
import ReactCrop from 'react-image-crop';
import 'react-image-crop/dist/ReactCrop.css';
import './ImageViewer.css';

const ImageViewer = ({ photos, onDelete, onEdit, onCrop }) => {
  const [selectedPhoto, setSelectedPhoto] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showCropModal, setShowCropModal] = useState(false);
  const [crop, setCrop] = useState();
  const [completedCrop, setCompletedCrop] = useState();
  const [cropImageSrc, setCropImageSrc] = useState('');

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

  const handleCrop = (photo, e) => {
    e.stopPropagation();
    setCropImageSrc(photo.url);
    setSelectedPhoto(photo);
    setShowCropModal(true);
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

  const onCropComplete = (c) => {
    setCompletedCrop(c);
  };

  const getCroppedImg = (image, crop, fileName) => {
    const canvas = document.createElement('canvas');
    const scaleX = image.naturalWidth / image.width;
    const scaleY = image.naturalHeight / image.height;
    canvas.width = crop.width;
    canvas.height = crop.height;
    const ctx = canvas.getContext('2d');

    ctx.drawImage(
      image,
      crop.x * scaleX,
      crop.y * scaleY,
      crop.width * scaleX,
      crop.height * scaleY,
      0,
      0,
      crop.width,
      crop.height
    );

    return new Promise((resolve) => {
      canvas.toBlob((blob) => {
        if (!blob) {
          console.error('Canvas is empty');
          return;
        }
        blob.name = fileName;
        const croppedImageUrl = URL.createObjectURL(blob);
        resolve(croppedImageUrl);
      }, 'image/jpeg');
    });
  };

  const handleSaveCrop = async () => {
    if (completedCrop?.width && completedCrop?.height) {
      const image = document.querySelector('.crop-modal-image');
      try {
        const croppedImageUrl = await getCroppedImg(
          image,
          completedCrop,
          'cropped.jpg'
        );
        
        // 收集裁剪参数
        const cropParams = {
          cropX: completedCrop.x,
          cropY: completedCrop.y,
          cropWidth: completedCrop.width,
          cropHeight: completedCrop.height
        };
        
        // 如果父组件提供了onCrop回调，则调用它
        if (onCrop) {
          onCrop(selectedPhoto.id, croppedImageUrl, cropParams);
        }
        
        setShowCropModal(false);
        setSelectedPhoto(null);
      } catch (e) {
        console.error('裁剪图片时出错:', e);
      }
    }
  };

  if (!photos || photos.length === 0) {
    return <div className="image-viewer-empty">暂无照片</div>;
  }

  // 批量下载功能
  const handleBatchDownload = () => {
    photos.forEach(photo => {
      const link = document.createElement('a');
      link.href = photo.url;
      link.download = `photo-${photo.id}.jpg`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    });
  };

  return (
    <div className="image-viewer">
      <div className="image-viewer-header">
        <h3>照片预览</h3>
        <button onClick={handleBatchDownload} className="batch-download-button">
          批量下载
        </button>
      </div>
      
      <div className="thumbnails-grid">
        {photos.map((photo, index) => (
          <div key={photo.id} className="thumbnail-wrapper">
            <img
              src={photo.url}
              alt={`照片 ${index + 1}`}
              className="thumbnail"
              onClick={() => openModal(photo, index)}
              loading="lazy"
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
                className="crop-button" 
                onClick={(e) => handleCrop(photo, e)}
                title="裁剪"
              >
                ✂️
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

      {selectedPhoto && !showCropModal && (
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
                  className="modal-crop-button" 
                  onClick={(e) => {
                    e.stopPropagation();
                    handleCrop(selectedPhoto, e);
                  }}
                >
                  裁剪
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

      {showCropModal && (
        <div className="modal-overlay">
          <div className="modal-content crop-modal">
            <h2>裁剪图片</h2>
            <div className="crop-container">
              <ReactCrop
                crop={crop}
                onChange={(c) => setCrop(c)}
                onComplete={onCropComplete}
                aspect={16 / 9}
              >
                <img
                  src={cropImageSrc}
                  alt="待裁剪图片"
                  className="crop-modal-image"
                />
              </ReactCrop>
            </div>
            <div className="crop-actions">
              <button 
                onClick={() => {
                  setShowCropModal(false);
                  setSelectedPhoto(null);
                }} 
                className="cancel-button"
              >
                取消
              </button>
              <button 
                onClick={handleSaveCrop} 
                className="save-button" 
                disabled={!completedCrop}
              >
                保存
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ImageViewer;