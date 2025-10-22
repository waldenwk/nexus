# Nexus - 社交网络平台需求文档

## 1. 简介

Nexus是一个现代化的社交网络平台，旨在为用户提供安全、便捷的社交体验。该平台将包含用户认证、内容分享、即时通讯、好友关系管理等功能模块。

## 2. 功能需求

### 2.1 用户管理
- 用户注册与登录
- 用户资料管理（头像、个人简介等）
- 密码重置
- 用户资料隐私设置

### 2.2 内容分享
- 发布文字内容（支持富文本格式）
- 发布图片内容
- 时间线展示（好友动态）
- 内容点赞与评论
- 内容隐私设置

### 2.3 相册功能
- 创建和管理相册
- 上传照片到相册
- 图片裁剪功能
- 图片编辑功能（亮度、对比度、饱和度调节，滤镜效果等）
- 图片查看和下载
- 删除照片

### 2.4 好友关系
- 好友搜索与添加
- 好友列表管理
- 好友状态显示（在线/离线）

### 2.5 即时通讯
- 实时消息发送与接收
- 消息历史记录
- 未读消息提示
- 多人聊天支持

### 2.6 通知系统
- 系统通知
- 社交互动通知（点赞、评论等）

### 2.7 隐私与安全
- 个人资料可见性设置
- 联系信息隐私设置
- 内容访问权限控制
- 好友关系验证

## 3. 非功能需求

### 3.1 性能要求
- 页面加载时间不超过3秒
- 支持同时在线用户数不少于10000人
- 消息延迟不超过1秒

### 3.2 安全要求
- 用户密码加密存储
- 敏感操作需要身份验证
- 防止SQL注入和XSS攻击
- 数据传输使用HTTPS加密

### 3.3 可用性要求
- 系统正常运行时间不低于99.9%
- 提供友好的用户界面
- 支持主流浏览器（Chrome、Firefox、Safari、Edge）

### 3.4 可扩展性要求
- 支持微服务架构
- 数据库设计考虑水平扩展
- 支持负载均衡部署

## 4. 技术栈

详细技术栈信息请参考[架构文档](ARCHITECTURE.md)。

## 5. 数据模型

### 5.1 用户(User)
- id: 用户唯一标识
- username: 用户名
- email: 邮箱
- password: 密码（加密存储）
- avatar: 头像URL
- bio: 个人简介
- created_at: 注册时间

### 5.2 帖子(Post)
- id: 帖子唯一标识
- user_id: 发布者ID
- content: 内容（支持富文本）
- created_at: 发布时间
- updated_at: 更新时间

### 5.3 相册(Album)
- id: 相册唯一标识
- user_id: 用户ID
- name: 相册名称
- description: 相册描述
- created_at: 创建时间
- updated_at: 更新时间

### 5.4 照片(Photo)
- id: 照片唯一标识
- album_id: 相册ID
- user_id: 用户ID
- url: 照片URL
- caption: 照片说明
- latitude: 纬度坐标
- longitude: 经度坐标
- created_at: 上传时间
- brightness: 亮度调整值
- contrast: 对比度调整值
- saturation: 饱和度调整值
- rotation: 旋转角度
- filter: 应用的滤镜
- crop_x: 裁剪起始X坐标
- crop_y: 裁剪起始Y坐标
- crop_width: 裁剪宽度
- crop_height: 裁剪高度

### 5.5 好友关系(Friendship)
- id: 关系唯一标识
- user_id: 用户ID
- friend_id: 好友ID
- status: 关系状态（pending/accepted/blocked）
- created_at: 建立时间

### 5.6 消息(Message)
- id: 消息唯一标识
- sender_id: 发送者ID
- receiver_id: 接收者ID
- content: 消息内容
- timestamp: 发送时间
- is_read: 是否已读

### 5.7 用户资料(UserProfile)
- id: 用户资料唯一标识
- user_id: 用户ID
- avatar_url: 头像URL
- bio: 个人简介
- city: 所在城市
- occupation: 职业
- interests: 兴趣爱好
- birthday: 生日
- gender: 性别
- phone: 电话号码
- contact_email: 联系邮箱
- profile_visibility: 个人资料可见性（public/friends_only/private）
- contact_visibility: 联系信息可见性（public/friends_only/private）
- created_at: 创建时间
- updated_at: 更新时间

## 6. API接口设计

### 6.1 认证相关
- POST /api/auth/login - 用户登录
- POST /api/auth/register - 用户注册

### 6.2 用户相关
- GET /api/users/profile - 获取用户资料
- PUT /api/users/profile - 更新用户资料
- GET /api/users/profile/{userId} - 获取指定用户资料（根据隐私设置）
- PUT /api/users/profile/privacy - 更新用户隐私设置

### 6.3 内容相关
- GET /api/posts/timeline - 获取时间线
- POST /api/posts - 发布新帖子
- POST /api/posts/{id}/like - 点赞帖子

### 6.4 相册相关
- POST /api/albums - 创建相册
- GET /api/albums/{id} - 获取相册详情
- PUT /api/albums/{id} - 更新相册
- DELETE /api/albums/{id} - 删除相册
- POST /api/albums/{id}/photos - 添加照片到相册
- GET /api/albums/{id}/photos - 获取相册中的照片
- PUT /api/albums/photos/{id} - 更新照片（包括编辑和裁剪信息）
- DELETE /api/photos/{id} - 删除照片

### 6.5 好友相关
- GET /api/friends - 获取好友列表
- POST /api/friends/request - 发送好友请求
- PUT /api/friends/{id}/accept - 接受好友请求

### 6.6 消息相关
- GET /api/messages/conversation/{id} - 获取会话消息
- POST /api/messages - 发送消息

## 7. 部署架构

详细部署架构信息请参考[部署文档](DEPLOYMENT.md)。

### 7.1 服务组成
- API网关：统一入口，负责路由和负载均衡
- 认证服务：处理用户认证和授权
- 用户服务：用户资料管理
- 内容服务：帖子和相册管理
- 好友服务：好友关系管理
- 消息服务：即时通讯功能
- 文件服务：文件存储和管理
- 搜索服务：内容搜索功能
- 推荐服务：个性化推荐算法
- 事件服务：处理系统事件和通知

### 7.2 数据存储
- MySQL：存储用户、帖子、相册等结构化数据
- Redis：缓存热点数据，如用户会话、好友列表等
- MinIO：存储用户上传的图片和文件

### 7.3 消息队列
- RabbitMQ：处理异步任务，如消息推送、邮件发送等

## 8. 测试策略

### 8.1 单元测试
- 覆盖核心业务逻辑
- 测试覆盖率目标：80%以上

### 8.2 集成测试
- 测试服务间接口调用
- 验证数据一致性

### 8.3 端到端测试
- 模拟用户行为进行全流程测试
- 包括UI交互和数据验证

## 9. 项目里程碑

### 9.1 第一阶段（基础功能）
- 用户认证系统
- 基础用户资料管理
- 帖子发布和时间线功能

### 9.2 第二阶段（社交功能增强）
- 好友关系管理
- 即时通讯功能
- 相册和图片管理功能

### 9.3 第三阶段（高级功能）
- 通知系统
- 搜索功能
- 个性化推荐

### 9.4 第四阶段（优化和完善）
- 性能优化
- 安全加固
- 用户体验改进
- 隐私功能完善