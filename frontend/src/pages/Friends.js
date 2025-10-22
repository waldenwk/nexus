import React, { useState, useEffect } from 'react';
import ApiService from '../services/api';
import './Friends.css';

const Friends = () => {
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchFriends();
  }, []);

  const fetchFriends = async () => {
    try {
      setLoading(true);
      const response = await ApiService.getFriends();
      if (response.success) {
        setFriends(response.data);
      } else {
        setError('获取朋友列表失败');
      }
    } catch (err) {
      setError('获取朋友列表时发生错误');
    } finally {
      setLoading(false);
    }
  };

  const filteredFriends = friends.filter(friend =>
    friend.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <div className="friends-page">加载中...</div>;
  }

  if (error) {
    return <div className="friends-page error-message">{error}</div>;
  }

  return (
    <div className="friends-page">
      <div className="friends-header">
        <h1>朋友</h1>
      </div>

      <div className="friends-search">
        <input
          type="text"
          placeholder="搜索朋友..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      <div className="friends-list">
        <h2>我的朋友 ({filteredFriends.length})</h2>
        {filteredFriends.length > 0 ? (
          <div className="friends-grid">
            {filteredFriends.map(friend => (
              <div key={friend.id} className="friend-card">
                <img src={friend.avatar} alt={friend.username} className="friend-avatar" />
                <div className="friend-info">
                  <h3>{friend.username}</h3>
                  <span className={`status ${friend.status === '在线' ? 'online' : friend.status === '离线' ? 'offline' : 'busy'}`}>
                    {friend.status}
                  </span>
                </div>
                <div className="friend-actions">
                  <button className="message-btn">发消息</button>
                  <button className="profile-btn">查看资料</button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p>没有找到匹配的朋友</p>
        )}
      </div>
    </div>
  );
};

export default Friends;