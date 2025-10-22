# CDN 配置和使用指南

## 概述

本文档介绍了如何在 Nexus 文件服务中配置和使用 CDN (Content Delivery Network) 功能。CDN 可以显著提高静态资源（如图片、视频等）的加载速度，改善用户体验。

## 配置参数

在 `application.yml` 文件中，可以通过以下参数配置 CDN：

```yaml
# CDN配置
cdn:
  # 是否启用CDN
  enabled: ${CDN_ENABLED:false}
  # CDN域名
  domain: ${CDN_DOMAIN:}
  # CDN协议
  protocol: ${CDN_PROTOCOL:https}
```

### 环境变量

- `CDN_ENABLED`: 是否启用 CDN (默认: false)
- `CDN_DOMAIN`: CDN 域名 (默认: 空)
- `CDN_PROTOCOL`: CDN 协议 (默认: https)

## 使用示例

### 启用 CDN

在 `application.yml` 中设置：

```yaml
cdn:
  enabled: true
  domain: your-cdn-domain.com
  protocol: https
```

或者通过环境变量：

```bash
export CDN_ENABLED=true
export CDN_DOMAIN=your-cdn-domain.com
export CDN_PROTOCOL=https
```

### CDN URL 生成

当 CDN 启用后，文件服务会自动为上传的文件生成 CDN URL 而不是 MinIO 预签名 URL。

例如：
- 文件名: `example.jpg`
- 存储桶: `nexus-files`
- CDN 域名: `cdn.example.com`

生成的 CDN URL 将是：
```
https://cdn.example.com/nexus-files/example.jpg
```

## 工作原理

1. 当 CDN 启用且配置有效时，文件服务会优先返回 CDN URL
2. 如果 CDN 未启用或配置无效，系统会回退到 MinIO 预签名 URL
3. 文件名会自动进行 URL 编码以处理特殊字符和空格

## 测试 CDN 配置

可以通过以下方式测试 CDN 配置是否正确：

1. 启用 CDN 并设置域名
2. 上传一个文件
3. 检查返回的 URL 是否为 CDN URL 格式
4. 访问该 URL 确认文件可以正常访问

## API端点

文件服务提供了以下与CDN相关的API端点：

1. `GET /api/cdn/status` - 获取CDN配置状态
2. `GET /api/files/url/{fileName}` - 获取文件URL（CDN URL或MinIO预签名URL）

## 故障排除

### CDN URL 无法访问

1. 检查 CDN 域名是否正确配置
2. 确认 CDN 服务是否正常运行
3. 检查文件是否已正确同步到 CDN

### 回退到 MinIO URL

如果 CDN 配置无效，系统会自动回退到 MinIO 预签名 URL，确保文件始终可以访问。