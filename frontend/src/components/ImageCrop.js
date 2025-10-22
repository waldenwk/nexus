import React, { useState, useCallback } from 'react';
import ReactCrop, { centerCrop, makeAspectCrop } from 'react-image-crop';
import 'react-image-crop/dist/ReactCrop.css';

const ImageCrop = ({ src, onComplete, onCancel }) => {
  const [crop, setCrop] = useState();
  const [completedCrop, setCompletedCrop] = useState();
  const [imgRef, setImgRef] = useState(null);

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

  const handleSave = async () => {
    if (completedCrop?.width && completedCrop?.height) {
      // 在实际应用中，这里会进行裁剪操作
      // 为简化起见，我们直接返回原图URL
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
            ref={setImgRef}
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