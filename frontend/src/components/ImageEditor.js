import React, { useState, useRef, useEffect } from 'react';
import './ImageEditor.css';

const ImageEditor = ({ src, onComplete, onCancel }) => {
  const canvasRef = useRef(null);
  const imgRef = useRef(null);
  const [brightness, setBrightness] = useState(100);
  const [contrast, setContrast] = useState(100);
  const [saturation, setSaturation] = useState(100);
  const [rotation, setRotation] = useState(0);
  const [filter, setFilter] = useState('none');

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    const img = imgRef.current;

    if (!img || !ctx) return;

    img.onload = () => {
      // 设置画布尺寸
      canvas.width = img.width;
      canvas.height = img.height;
      
      // 应用滤镜和变换
      applyFilters();
    };

    if (img.complete) {
      img.onload();
    }
  }, [brightness, contrast, saturation, rotation, filter]);

  const applyFilters = () => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    const img = imgRef.current;

    if (!img || !ctx) return;

    // 清除画布
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 保存当前状态
    ctx.save();

    // 移动到中心点进行旋转
    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.rotate((rotation * Math.PI) / 180);
    
    // 应用滤镜
    let filterValue = `brightness(${brightness}%) contrast(${contrast}%) saturate(${saturation}%)`;
    
    switch (filter) {
      case 'grayscale':
        filterValue += ' grayscale(100%)';
        break;
      case 'sepia':
        filterValue += ' sepia(100%)';
        break;
      case 'invert':
        filterValue += ' invert(100%)';
        break;
      case 'blur':
        filterValue += ' blur(5px)';
        break;
      default:
        break;
    }
    
    ctx.filter = filterValue;

    // 绘制图片
    ctx.drawImage(
      img,
      -img.width / 2,
      -img.height / 2,
      img.width,
      img.height
    );

    // 恢复状态
    ctx.restore();
  };

  const handleSave = () => {
    const canvas = canvasRef.current;
    try {
      const editedImageUrl = canvas.toDataURL('image/jpeg');
      // 收集编辑参数
      const editParams = {
        brightness,
        contrast,
        saturation,
        rotation,
        filter
      };
      onComplete(editedImageUrl, editParams);
    } catch (e) {
      console.error('保存编辑图片时出错:', e);
      onComplete(src); // 出错时使用原图
    }
  };

  const handleDownload = () => {
    const canvas = canvasRef.current;
    try {
      const link = document.createElement('a');
      link.download = 'edited-image.jpg';
      link.href = canvas.toDataURL('image/jpeg');
      link.click();
    } catch (e) {
      console.error('下载图片时出错:', e);
    }
  };

  const handleReset = () => {
    setBrightness(100);
    setContrast(100);
    setSaturation(100);
    setRotation(0);
    setFilter('none');
  };

  return (
    <div className="image-editor">
      <h2>编辑图片</h2>
      
      <div className="editor-container">
        <div className="image-preview">
          <canvas ref={canvasRef} className="editor-canvas" />
          <img ref={imgRef} src={src} alt="原图" style={{ display: 'none' }} />
        </div>
        
        <div className="editor-controls">
          <div className="control-group">
            <label>亮度: {brightness}%</label>
            <input
              type="range"
              min="0"
              max="200"
              value={brightness}
              onChange={(e) => setBrightness(e.target.value)}
              className="control-slider"
            />
          </div>
          
          <div className="control-group">
            <label>对比度: {contrast}%</label>
            <input
              type="range"
              min="0"
              max="200"
              value={contrast}
              onChange={(e) => setContrast(e.target.value)}
              className="control-slider"
            />
          </div>
          
          <div className="control-group">
            <label>饱和度: {saturation}%</label>
            <input
              type="range"
              min="0"
              max="200"
              value={saturation}
              onChange={(e) => setSaturation(e.target.value)}
              className="control-slider"
            />
          </div>
          
          <div className="control-group">
            <label>旋转: {rotation}°</label>
            <input
              type="range"
              min="0"
              max="360"
              value={rotation}
              onChange={(e) => setRotation(e.target.value)}
              className="control-slider"
            />
          </div>
          
          <div className="control-group">
            <label>滤镜:</label>
            <select 
              value={filter} 
              onChange={(e) => setFilter(e.target.value)}
              className="filter-select"
            >
              <option value="none">无</option>
              <option value="grayscale">灰度</option>
              <option value="sepia">怀旧</option>
              <option value="invert">反色</option>
              <option value="blur">模糊</option>
            </select>
          </div>
          
          <div className="control-buttons">
            <button onClick={handleReset} className="reset-button">
              重置
            </button>
          </div>
        </div>
      </div>
      
      <div className="editor-actions">
        <button onClick={onCancel} className="cancel-button">
          取消
        </button>
        <button onClick={handleDownload} className="download-button">
          下载
        </button>
        <button onClick={handleSave} className="save-button">
          保存
        </button>
      </div>
    </div>
  );
};

export default ImageEditor;