// 国际化服务
class I18nService {
  constructor() {
    // 默认语言
    this.defaultLanguage = 'zh';
    
    // 支持的语言列表
    this.supportedLanguages = ['zh', 'en'];
    
    // 语言包
    this.translations = {
      zh: {
        // 通用
        'common.loading': '加载中...',
        'common.error': '出错了',
        'common.save': '保存',
        'common.cancel': '取消',
        'common.confirm': '确认',
        'common.delete': '删除',
        'common.edit': '编辑',
        'common.create': '创建',
        'common.search': '搜索',
        
        // 导航
        'nav.home': '首页',
        'nav.profile': '个人资料',
        'nav.friends': '朋友',
        'nav.messages': '消息',
        'nav.photos': '照片',
        'nav.settings': '设置',
        
        // 登录/注册
        'auth.login': '登录',
        'auth.register': '注册',
        'auth.email': '邮箱',
        'auth.password': '密码',
        'auth.confirmPassword': '确认密码',
        'auth.username': '用户名',
        'auth.forgotPassword': '忘记密码？',
        'auth.noAccount': '没有账户？',
        'auth.hasAccount': '已有账户？',
        
        // 个人资料
        'profile.title': '个人资料',
        'profile.username': '用户名',
        'profile.email': '邮箱',
        'profile.bio': '个人简介',
        'profile.joinDate': '加入时间',
        
        // 朋友圈
        'timeline.title': '时间轴',
        'timeline.placeholder': '分享你的想法...',
        'timeline.post': '发布',
        
        // 朋友
        'friends.title': '朋友',
        'friends.search': '搜索朋友...',
        
        // 消息
        'messages.title': '消息',
        'messages.typeMessage': '输入消息...',
        
        // 照片
        'photos.albums': '相册',
        'photos.createAlbum': '创建相册',
        'photos.upload': '上传照片',
        'photos.edit': '编辑照片',
        'photos.crop': '裁剪照片',
        
        // 设置
        'settings.title': '设置',
        'settings.language': '语言',
        'settings.theme': '主题',
        'settings.notifications': '通知',
        
        // 错误消息
        'error.network': '网络错误，请稍后重试',
        'error.login': '登录失败，请检查邮箱和密码',
        'error.register': '注册失败，请检查输入信息',
        'error.passwordMismatch': '两次输入的密码不一致'
      },
      en: {
        // 通用
        'common.loading': 'Loading...',
        'common.error': 'Error',
        'common.save': 'Save',
        'common.cancel': 'Cancel',
        'common.confirm': 'Confirm',
        'common.delete': 'Delete',
        'common.edit': 'Edit',
        'common.create': 'Create',
        'common.search': 'Search',
        
        // 导航
        'nav.home': 'Home',
        'nav.profile': 'Profile',
        'nav.friends': 'Friends',
        'nav.messages': 'Messages',
        'nav.photos': 'Photos',
        'nav.settings': 'Settings',
        
        // 登录/注册
        'auth.login': 'Login',
        'auth.register': 'Register',
        'auth.email': 'Email',
        'auth.password': 'Password',
        'auth.confirmPassword': 'Confirm Password',
        'auth.username': 'Username',
        'auth.forgotPassword': 'Forgot password?',
        'auth.noAccount': 'No account?',
        'auth.hasAccount': 'Have an account?',
        
        // 个人资料
        'profile.title': 'Profile',
        'profile.username': 'Username',
        'profile.email': 'Email',
        'profile.bio': 'Bio',
        'profile.joinDate': 'Join Date',
        
        // 朋友圈
        'timeline.title': 'Timeline',
        'timeline.placeholder': 'Share your thoughts...',
        'timeline.post': 'Post',
        
        // 朋友
        'friends.title': 'Friends',
        'friends.search': 'Search friends...',
        
        // 消息
        'messages.title': 'Messages',
        'messages.typeMessage': 'Type a message...',
        
        // 照片
        'photos.albums': 'Albums',
        'photos.createAlbum': 'Create Album',
        'photos.upload': 'Upload Photo',
        'photos.edit': 'Edit Photo',
        'photos.crop': 'Crop Photo',
        
        // 设置
        'settings.title': 'Settings',
        'settings.language': 'Language',
        'settings.theme': 'Theme',
        'settings.notifications': 'Notifications',
        
        // 错误消息
        'error.network': 'Network error, please try again later',
        'error.login': 'Login failed, please check email and password',
        'error.register': 'Registration failed, please check input information',
        'error.passwordMismatch': 'Passwords do not match'
      }
    };
    
    // 当前语言
    this.currentLanguage = this.getStoredLanguage() || this.defaultLanguage;
  }
  
  // 获取存储的语言设置
  getStoredLanguage() {
    try {
      return localStorage.getItem('language');
    } catch (e) {
      return null;
    }
  }
  
  // 保存语言设置
  setStoredLanguage(language) {
    try {
      localStorage.setItem('language', language);
    } catch (e) {
      // 忽略存储错误
    }
  }
  
  // 设置当前语言
  setLanguage(language) {
    if (this.supportedLanguages.includes(language)) {
      this.currentLanguage = language;
      this.setStoredLanguage(language);
      // 触发语言更改事件
      window.dispatchEvent(new CustomEvent('languageChanged', { detail: language }));
      return true;
    }
    return false;
  }
  
  // 获取当前语言
  getCurrentLanguage() {
    return this.currentLanguage;
  }
  
  // 获取支持的语言列表
  getSupportedLanguages() {
    return this.supportedLanguages;
  }
  
  // 翻译文本
  t(key, params = {}) {
    // 获取当前语言的翻译
    const translation = this.translations[this.currentLanguage]?.[key] || 
                       this.translations[this.defaultLanguage]?.[key] || 
                       key;
    
    // 处理参数替换
    let result = translation;
    Object.keys(params).forEach(param => {
      const regex = new RegExp(`{${param}}`, 'g');
      result = result.replace(regex, params[param]);
    });
    
    return result;
  }
}

// 创建单例实例
const i18n = new I18nService();

// 导出函数供外部使用
export const t = i18n.t.bind(i18n);
export const setLanguage = i18n.setLanguage.bind(i18n);
export const getCurrentLanguage = i18n.getCurrentLanguage.bind(i18n);
export const getSupportedLanguages = i18n.getSupportedLanguages.bind(i18n);

export default i18n;