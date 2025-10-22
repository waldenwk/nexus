import React, { useEffect, useRef } from 'react';
import './MapViewer.css';

const MapViewer = ({ photos, onPhotoSelect }) => {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const markersRef = useRef([]);

  useEffect(() => {
    // 初始化地图
    if (window.mapboxgl && mapRef.current) {
      // 设置Mapbox访问令牌
      window.mapboxgl.accessToken = process.env.REACT_APP_MAPBOX_ACCESS_TOKEN || 'pk.eyJ1IjoibmV4dXMtZGV2IiwiYSI6ImNsb21vZnJ5eTAwMHQya3F0cGZ6d3N6Y2wifQ.6D28J8saP00F8e3c3c3c3c3c3c3c3c3c3c';

      // 创建地图实例
      const map = new window.mapboxgl.Map({
        container: mapRef.current,
        style: 'mapbox://styles/mapbox/streets-v12',
        center: [104.1954, 35.8617], // 中国中心坐标
        zoom: 3
      });

      // 添加导航控件
      map.addControl(new window.mapboxgl.NavigationControl(), 'top-right');

      // 存储地图实例
      mapInstanceRef.current = map;

      // 清理函数
      return () => {
        map.remove();
      };
    }
  }, []);

  useEffect(() => {
    // 当照片数据更新时，在地图上添加标记
    if (mapInstanceRef.current && photos && photos.length > 0) {
      // 清除现有标记
      markersRef.current.forEach(marker => marker.remove());
      markersRef.current = [];

      // 添加新标记
      photos.forEach(photo => {
        // 检查照片是否有地理位置信息
        if (photo.latitude && photo.longitude) {
          // 创建标记元素
          const el = document.createElement('div');
          el.className = 'marker';
          el.style.backgroundImage = `url(${photo.url})`;
          el.style.width = '40px';
          el.style.height = '40px';
          el.style.backgroundSize = 'cover';
          el.style.borderRadius = '50%';
          el.style.cursor = 'pointer';
          el.style.border = '2px solid #fff';
          el.style.boxShadow = '0 0 0 2px #3b82f6';

          // 创建标记
          const marker = new window.mapboxgl.Marker({
            element: el
          })
            .setLngLat([photo.longitude, photo.latitude])
            .addTo(mapInstanceRef.current);

          // 添加点击事件
          el.addEventListener('click', () => {
            if (onPhotoSelect) {
              onPhotoSelect(photo);
            }
          });

          // 存储标记引用
          markersRef.current.push(marker);
        }
      });

      // 如果有带地理位置的照片，调整地图视野
      const geoPhotos = photos.filter(photo => photo.latitude && photo.longitude);
      if (geoPhotos.length > 0) {
        const bounds = new window.mapboxgl.LngLatBounds();
        geoPhotos.forEach(photo => {
          bounds.extend([photo.longitude, photo.latitude]);
        });
        mapInstanceRef.current.fitBounds(bounds, { padding: 50 });
      }
    }
  }, [photos, onPhotoSelect]);

  return (
    <div className="map-viewer">
      <div ref={mapRef} className="map-container" />
      {(!photos || photos.length === 0) && (
        <div className="map-placeholder">
          <p>暂无带地理位置信息的照片</p>
        </div>
      )}
    </div>
  );
};

export default MapViewer;