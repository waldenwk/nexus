import React, { useState, useEffect } from 'react';
import ApiService from '../services/api';
import './Profile.css';

const Profile = () => {
  const [activeTab, setActiveTab] = useState('posts');
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // 模拟帖子数据
  const posts = [
    { id: 1, content: '今天在图书馆学习了一整天，收获满满！', timestamp: '2小时前' },
    { id: 2, content: '参加了校园技术分享会，学到了很多新知识。', timestamp: '1天前' },
    { id: 3, content: '新学期开始了，加油！', timestamp: '1周前' }
  ];

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await ApiService.getProfile();
        if (response.success) {
          setUser(response.data);
        } else {
          setError('获取用户资料失败');
        }
      } catch (err) {
        setError('获取用户资料时发生错误');
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) {
    return <div className="profile">加载中...</div>;
  }

  if (error) {
    return <div className="profile error-message">{error}</div>;
  }

  return (
    <div className="profile">
      <div className="profile-header">
        <img src={user.avatar} alt="头像" className="avatar" />
        <div className="profile-info">
          <h1>{user.username}</h1>
          <p>{user.bio}</p>
          <p>邮箱: {user.email}</p>
          <p>注册时间: {user.joinDate}</p>
        </div>
      </div>

      <div className="profile-content">
        <div className="tabs">
          <button 
            className={activeTab === 'posts' ? 'tab active' : 'tab'}
            onClick={() => setActiveTab('posts')}
          >
            我的帖子
          </button>
          <button 
            className={activeTab === 'friends' ? 'tab active' : 'tab'}
            onClick={() => setActiveTab('friends')}
          >
            朋友
          </button>
          <button 
            className={activeTab === 'settings' ? 'tab active' : 'tab'}
            onClick={() => setActiveTab('settings')}
          >
            设置
          </button>
        </div>

        <div className="tab-content">
          {activeTab === 'posts' && (
            <div className="posts">
              <h2>我的帖子</h2>
              {posts.map(post => (
                <div key={post.id} className="post">
                  <p>{post.content}</p>
                  <span className="timestamp">{post.timestamp}</span>
                </div>
              ))}
            </div>
          )}

          {activeTab === 'friends' && (
            <div className="friends">
              <h2>我的朋友</h2>
              <p>朋友列表功能正在开发中...</p>
            </div>
          )}

          {activeTab === 'settings' && (
            <div className="settings">
              <h2>账户设置</h2>
              <p>账户设置功能正在开发中...</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;