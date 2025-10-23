// 主题服务
class ThemeService {
  constructor() {
    // 默认主题
    this.defaultTheme = 'default';
    
    // 支持的主题列表
    this.supportedThemes = [
      'default',     // 默认主题
      'dark',        // 深色主题
      'light',       // 浅色主题
      'blue',        // 蓝色主题
      'green',       // 绿色主题
      'purple',      // 紫色主题
      'orange'       // 橙色主题
    ];
    
    // 主题图标映射
    this.themeIcons = {
      'default': '🎨',
      'dark': '🌙',
      'light': '☀️',
      'blue': '💙',
      'green': '💚',
      'purple': '💜',
      'orange': '🧡'
    };
    
    // 当前主题
    this.currentTheme = this.getStoredTheme() || this.defaultTheme;
    
    // 应用当前主题
    this.applyTheme(this.currentTheme);
  }
  
  // 获取存储的主题设置
  getStoredTheme() {
    try {
      return localStorage.getItem('theme');
    } catch (e) {
      return null;
    }
  }
  
  // 保存主题设置
  setStoredTheme(theme) {
    try {
      localStorage.setItem('theme', theme);
    } catch (e) {
      // 忽略存储错误
    }
  }
  
  // 设置当前主题
  setTheme(theme) {
    if (this.supportedThemes.includes(theme)) {
      this.currentTheme = theme;
      this.setStoredTheme(theme);
      this.applyTheme(theme);
      // 触发主题更改事件
      window.dispatchEvent(new CustomEvent('themeChanged', { detail: { theme, icon: this.themeIcons[theme] } }));
      return true;
    }
    return false;
  }
  
  // 获取当前主题
  getCurrentTheme() {
    return this.currentTheme;
  }
  
  // 获取当前主题图标
  getCurrentThemeIcon() {
    return this.themeIcons[this.currentTheme] || this.themeIcons[this.defaultTheme];
  }
  
  // 获取主题图标
  getThemeIcon(theme) {
    return this.themeIcons[theme] || this.themeIcons[this.defaultTheme];
  }
  
  // 获取支持的主题列表
  getSupportedThemes() {
    return this.supportedThemes;
  }
  
  // 获取所有主题和图标映射
  getThemeIcons() {
    return this.themeIcons;
  }
  
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
}

// 创建单例实例
const themeService = new ThemeService();

// 导出函数供外部使用
export const setTheme = themeService.setTheme.bind(themeService);
export const getCurrentTheme = themeService.getCurrentTheme.bind(themeService);
export const getSupportedThemes = themeService.getSupportedThemes.bind(themeService);
export const getThemeIcon = themeService.getThemeIcon.bind(themeService);
export const getCurrentThemeIcon = themeService.getCurrentThemeIcon.bind(themeService);
export const getThemeIcons = themeService.getThemeIcons.bind(themeService);

export default themeService;