# Nexus 前端项目

这是一个基于 React 的社交网络前端应用，为 Nexus 校园社交平台提供用户界面。

## 项目结构

```
src/
├── components/        # 可复用的UI组件
├── pages/             # 页面组件
├── services/          # API服务
├── App.js             # 主应用组件
├── index.js           # 应用入口
└── ...
```

## 技术栈

- React 18
- React Router v6
- CSS Modules

## 快速开始

1. 安装依赖:
   ```
   npm install
   ```

2. 启动开发服务器:
   ```
   npm start
   ```

3. 构建生产版本:
   ```
   npm run build
   ```

## 部署

前端应用已容器化，可以通过 Docker 进行部署。在项目根目录的 `docker-compose-services.yml` 文件中已经包含了前端服务的配置。

构建和启动前端服务:
```bash
docker-compose -f docker-compose-services.yml up -d frontend
```

前端应用将运行在 http://localhost

## 功能特性

- 用户认证 (登录/注册)
- 个人资料管理
- 时间轴/动态发布
- 好友管理
- 实时消息系统
- 响应式设计

## 开发指南

### 添加新页面

1. 在 `src/pages/` 目录下创建新页面组件
2. 在 `src/App.js` 中导入并添加路由

### 添加新组件

1. 在 `src/components/` 目录下创建新组件
2. 在需要使用的页面或组件中导入

### API 集成

所有 API 调用应通过 `src/services/api.js` 进行，该文件包含了模拟的 API 服务。