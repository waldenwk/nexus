# 主题系统 (Themes)

Nexus 平台支持多种主题，用户可以根据个人喜好选择不同的界面风格。

## 当前支持的主题

- **Default (🎨)** - 默认主题，经典的蓝白配色方案
- **Dark (🌙)** - 深色主题，适合夜间使用，减少眼部疲劳
- **Light (☀️)** - 浅色主题，简洁明亮的界面风格
- **Blue (💙)** - 蓝色主题，专业感强的蓝色调配色方案
- **Green (💚)** - 绿色主题，自然清新的绿色调配色方案
- **Purple (💜)** - 紫色主题，优雅神秘的紫色调配色方案
- **Orange (🧡)** - 橙色主题，活力四射的橙色调配色方案

## 技术实现

### 核心文件

- `frontend/src/services/theme.js` - 主题服务核心文件
- `frontend/src/components/Header.js` - 主题选择器
- `frontend/src/App.js` - 应用程序主文件，处理主题变更事件
- `frontend/src/index.css` - CSS变量定义和基础样式

### CSS变量系统

平台使用CSS变量实现主题系统，每种主题都定义了一套完整的CSS变量：

```css
:root {
  /* Default theme variables */
  --primary-color: #007bff;
  --secondary-color: #6c757d;
  --background-color: #f0f2f5;
  --text-primary: #212529;
  --text-secondary: #6c757d;
  --border-color: #dee2e6;
  --header-bg: #ffffff;
  --sidebar-bg: #ffffff;
}

.theme-dark {
  /* Dark theme variables */
  --primary-color: #0d6efd;
  --secondary-color: #6c757d;
  --background-color: #121212;
  --text-primary: #f8f9fa;
  --text-secondary: #adb5bd;
  --border-color: #495057;
  --header-bg: #1e1e1e;
  --sidebar-bg: #1e1e1e;
}
```

### 主题切换机制

主题切换通过JavaScript动态切换<body>元素的类名来实现：

```javascript
// 应用主题
applyTheme(theme) {
  // 移除所有主题类
  document.body.className = document.body.className
    .split(' ')
    .filter(className => !className.startsWith('theme-'))
    .join(' ');

  // 添加当前主题类
  document.body.classList.add(`theme-${theme}`);
}
```

### 主题持久化

主题设置会保存在浏览器的 localStorage 中，用户下次访问时会自动使用上次选择的主题：

```javascript
// 保存主题设置
setStoredTheme(theme) {
  try {
    localStorage.setItem('theme', theme);
  } catch (e) {
    // 忽略存储错误
  }
}

// 获取存储的主题设置
getStoredTheme() {
  try {
    return localStorage.getItem('theme');
  } catch (e) {
    return null;
  }
}
```

## 主题切换

用户可以通过以下方式切换主题：

1. 页面头部的主题选择器
2. 设置页面的主题选项

## 添加新主题

要添加新主题，请按照以下步骤操作：

1. 在 `frontend/src/services/theme.js` 的 `supportedThemes` 数组中添加新主题名称：

```javascript
this.supportedThemes = [
  'default',
  'dark', 
  'light',
  'blue',
  'green',
  'purple',
  'orange',
  'new-theme'  // 添加新主题
];
```

2. 在 `frontend/src/services/theme.js` 的 `themeIcons` 对象中添加新主题图标：

```javascript
this.themeIcons = {
  'default': '🎨',
  'dark': '🌙',
  'light': '☀️',
  'blue': '💙',
  'green': '💚',
  'purple': '💜',
  'orange': '🧡',
  'new-theme': '🌟'  // 添加新主题图标
};
```

3. 在 `frontend/src/index.css` 中添加新主题的CSS变量定义：

```css
.theme-new-theme {
  --primary-color: #ff0000;
  --secondary-color: #6c757d;
  --background-color: #ffffff;
  --text-primary: #212529;
  --text-secondary: #6c757d;
  --border-color: #dee2e6;
  --header-bg: #ffffff;
  --sidebar-bg: #ffffff;
}
```

4. 在主题选择器中会自动显示新主题选项。

## 主题与国际化集成

主题系统与国际化系统紧密集成，为用户提供一致的用户体验：

1. 语言选择器和主题选择器并排显示在页面头部
2. 两种设置都保存在localStorage中
3. 支持同时切换语言和主题
4. 提供统一的设置界面

## 扩展建议

1. **动态主题生成**：基于用户偏好自动生成个性化主题
2. **主题分享**：允许用户创建和分享自定义主题
3. **动画过渡**：为主题切换添加平滑的过渡动画
4. **系统主题检测**：自动检测用户系统的主题偏好并应用相应主题