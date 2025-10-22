// 模拟API服务
class ApiService {
  // 模拟登录
  static async login(credentials) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...',
            user: {
              id: 1,
              username: credentials.email.split('@')[0],
              email: credentials.email
            }
          }
        });
      }, 1000);
    });
  }

  // 模拟注册
  static async register(userData) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...',
            user: {
              id: Date.now(),
              username: userData.username,
              email: userData.email
            }
          }
        });
      }, 1000);
    });
  }

  // 模拟获取用户资料
  static async getProfile() {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            id: 1,
            username: '张三',
            email: 'zhangsan@example.com',
            avatar: 'https://via.placeholder.com/150',
            bio: '热爱技术的大学生',
            joinDate: '2023-01-15'
          }
        });
      }, 500);
    });
  }

  // 模拟获取时间轴
  static async getTimeline() {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: [
            {
              id: 1,
              username: '李四',
              avatar: 'https://via.placeholder.com/40',
              content: '刚刚完成了数据结构作业，感觉还不错！',
              timestamp: '10分钟前',
              likes: 5,
              comments: 2
            },
            {
              id: 2,
              username: '王五',
              avatar: 'https://via.placeholder.com/40',
              content: '推荐一本好书《深入理解计算机系统》，对理解计算机底层原理很有帮助。',
              timestamp: '1小时前',
              likes: 12,
              comments: 4
            }
          ]
        });
      }, 800);
    });
  }

  // 模拟发布帖子
  static async createPost(content) {
    // 模拟API调用
    // content参数现在可以包含HTML格式的富文本内容
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            id: Date.now(),
            username: '我',
            avatar: 'https://via.placeholder.com/40',
            content: content, // 富文本内容直接存储
            timestamp: '刚刚',
            likes: 0,
            comments: 0
          }
        });
      }, 500);
    });
  }

  // 模拟获取朋友列表
  static async getFriends() {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: [
            { id: 1, username: '李四', avatar: 'https://via.placeholder.com/50', status: '在线' },
            { id: 2, username: '王五', avatar: 'https://via.placeholder.com/50', status: '离线' },
            { id: 3, username: '赵六', avatar: 'https://via.placeholder.com/50', status: '忙碌' },
            { id: 4, username: '孙七', avatar: 'https://via.placeholder.com/50', status: '在线' }
          ]
        });
      }, 600);
    });
  }

  // 模拟获取消息
  static async getMessages(conversationId) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: [
            { id: 1, sender: '李四', content: '你好，在吗？', timestamp: '10:25 AM' },
            { id: 2, sender: '我', content: '在的，有什么可以帮助你的吗？', timestamp: '10:26 AM' },
            { id: 3, sender: '李四', content: '我想问一下关于明天的课程安排', timestamp: '10:28 AM' },
            { id: 4, sender: '我', content: '明天上午是数据结构，下午是英语课', timestamp: '10:29 AM' }
          ]
        });
      }, 500);
    });
  }

  // 模拟创建相册
  static async createAlbum(name, description) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            id: Date.now(),
            name: name,
            description: description,
            createdAt: new Date().toISOString()
          }
        });
      }, 500);
    });
  }

  // 模拟上传照片
  static async uploadPhoto(file, location = null) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            url: 'https://via.placeholder.com/600x400?text=' + encodeURIComponent(file.name),
            id: Date.now(),
            latitude: location?.latitude || null,
            longitude: location?.longitude || null
          }
        });
      }, 1000);
    });
  }

  // 模拟编辑照片
  static async editPhoto(photoId, editedImage, editParams) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            url: editedImage,
            id: photoId,
            ...editParams
          }
        });
      }, 800);
    });
  }

  // 模拟裁剪照片
  static async cropPhoto(photoId, croppedImage, cropParams) {
    // 模拟API调用
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          data: {
            url: croppedImage,
            id: photoId,
            ...cropParams
          }
        });
      }, 800);
    });
  }
}

export default ApiService;