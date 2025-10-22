import React, { useState, useCallback, useRef } from 'react';
import ReactCrop, { centerCrop, makeAspectCrop } from 'react-image-crop';
import 'react-image-crop/dist/ReactCrop.css';

const ImageCrop = ({ src, onComplete, onCancel }) => {
  const [crop, setCrop] = useState();
  const [completedCrop, setCompletedCrop] = useState();
  const imgRef = useRef(null);

  const onImageLoad = useCallback((e) => {
    const { width, height } = e.currentTarget;
    const crop = centerCrop(
      makeAspectCrop(
        {
          unit: '%',
          width: 90,
        },
        16 / 9,
        width,
        height
      ),
      width,
      height
    );
    
    setCrop(crop);
  }, []);

  const handleComplete = useCallback((c) => {
    setCompletedCrop(c);
  }, []);

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

  const handleSave = async () => {
    if (imgRef.current && completedCrop?.width && completedCrop?.height) {
      try {
        const croppedImageUrl = await getCroppedImg(
          imgRef.current,
          completedCrop,
          'cropped.jpg'
        );
        onComplete(croppedImageUrl);
      } catch (e) {
        console.error('裁剪图片时出错:', e);
        onComplete(src); // 出错时使用原图
      }
    } else {
      onComplete(src);
    }
  };

  return (
    <div className="image-crop">
      <h2>裁剪图片</h2>
      <div className="crop-container">
        <ReactCrop
          crop={crop}
          onChange={(c) => setCrop(c)}
          onComplete={handleComplete}
          aspect={16 / 9}
        >
          <img
            ref={imgRef}
            src={src}
            onLoad={onImageLoad}
            alt="待裁剪图片"
            style={{ maxHeight: '70vh' }}
          />
        </ReactCrop>
      </div>
      
      <div className="crop-actions">
        <button onClick={onCancel} className="cancel-button">
          取消
        </button>
        <button onClick={handleSave} className="save-button" disabled={!completedCrop}>
          保存
        </button>
      </div>
    </div>
  );
};

export default ImageCrop;