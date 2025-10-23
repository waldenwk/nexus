# CDN 使用示例

## 概述

本文档提供了在Nexus文件服务中使用CDN功能的完整示例。

## 启用CDN配置

### 1. 在 application.yml 中配置

```yaml
cdn:
  enabled: true
  domain: cdn.yourdomain.com
  protocol: https
```

### 2. 通过环境变量配置

```bash
export CDN_ENABLED=true
export CDN_DOMAIN=cdn.yourdomain.com
export CDN_PROTOCOL=https
```

## 上传文件并获取CDN URL

### 使用API上传文件

```bash
curl -X POST \
  http://localhost:8088/api/files/upload \
  -H 'content-type: multipart/form-data' \
  -F 'file=@/path/to/your/image.jpg'
```

响应将返回CDN URL（如果CDN已启用）：
```
https://cdn.yourdomain.com/nexus-files/a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8.jpg
```

### 使用JavaScript上传文件

```javascript
const fileInput = document.getElementById('fileInput');
const file = fileInput.files[0];

const formData = new FormData();
formData.append('file', file);

fetch('http://localhost:8088/api/files/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.text())
.then(url => {
  console.log('CDN URL:', url);
  // 使用返回的CDN URL显示图片
  document.getElementById('image').src = url;
})
.catch(error => {
  console.error('Upload error:', error);
});
```

## 检查CDN状态

### 使用API检查CDN状态

```bash
curl -X GET http://localhost:8088/api/cdn/status
```

响应示例：
```json
{
  "enabled": true,
  "domain": "cdn.yourdomain.com",
  "protocol": "https",
  "valid": true,
  "baseUrl": "https://cdn.yourdomain.com"
}
```

## 直接获取文件URL

如果只需要获取文件URL而不需要上传新文件：

### 使用API获取文件URL

```bash
curl -X GET http://localhost:8088/api/files/url/your-file-name.jpg
```

响应示例：
```json
{
  "fileName": "your-file-name.jpg",
  "url": "https://cdn.yourdomain.com/nexus-files/your-file-name.jpg"
}
```

### 使用JavaScript获取文件URL

```javascript
// 获取文件URL
async function getFileUrl(fileName) {
  try {
    const response = await fetch(`http://localhost:8088/api/files/url/${fileName}`);
    const data = await response.json();
    return data.url;
  } catch (error) {
    console.error('Error getting file URL:', error);
    return null;
  }
}

// 使用示例
getFileUrl('example.jpg').then(url => {
  if (url) {
    console.log('File URL:', url);
    // 在页面中使用URL
    document.getElementById('image').src = url;
  }
});
```

## 处理特殊字符

CDN URL构建器会自动处理文件名中的特殊字符：

原始文件名：
```
my photo (1).jpg
```

编码后的CDN URL：
```
https://cdn.yourdomain.com/nexus-files/my%20photo%20%281%29.jpg
```

## 回退机制

当CDN未启用或配置无效时，系统会自动回退到MinIO预签名URL：

```
http://localhost:9000/nexus-files/a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin...
```

## 前端集成示例

### React组件示例

```jsx
import React, { useState } from 'react';

const ImageUploader = () => {
  const [imageUrl, setImageUrl] = useState('');
  const [uploading, setUploading] = useState(false);

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    setUploading(true);
    
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('http://localhost:8088/api/files/upload', {
        method: 'POST',
        body: formData
      });
      
      if (response.ok) {
        const url = await response.text();
        setImageUrl(url);
      } else {
        console.error('Upload failed');
      }
    } catch (error) {
      console.error('Upload error:', error);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <input 
        type="file" 
        onChange={handleFileUpload} 
        accept="image/*" 
        disabled={uploading}
      />
      {uploading && <p>Uploading...</p>}
      {imageUrl && (
        <div>
          <h3>Uploaded Image:</h3>
          <img src={imageUrl} alt="Uploaded" style={{maxWidth: '100%'}} />
        </div>
      )}
    </div>
  );
};

export default ImageUploader;
```

## 故障排除

### 1. CDN URL返回404

检查：
- CDN服务是否正常运行
- 文件是否已正确上传到MinIO
- CDN是否已正确配置以从MinIO拉取文件

### 2. 返回MinIO URL而不是CDN URL

检查：
- CDN是否已启用 (`cdn.enabled=true`)
- CDN域名是否已正确配置
- CDN配置是否有效

### 3. 特殊字符处理问题

如果URL中有特殊字符导致问题，请确保：
- 使用正确的URL编码
- CDN服务支持编码后的URL