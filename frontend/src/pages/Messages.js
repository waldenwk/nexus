import React, { useState, useEffect } from 'react';
import ApiService from '../services/api';
import './Messages.css';

const Messages = () => {
  const [conversations, setConversations] = useState([
    { id: 1, username: '李四', avatar: 'https://via.placeholder.com/40', lastMessage: '你好，在吗？', timestamp: '10:30 AM', unread: 2 },
    { id: 2, username: '王五', avatar: 'https://via.placeholder.com/40', lastMessage: '明天一起吃饭吗？', timestamp: '昨天', unread: 0 },
    { id: 3, username: '赵六', avatar: 'https://via.placeholder.com/40', lastMessage: '谢谢你的帮助！', timestamp: '昨天', unread: 0 }
  ]);

  const [activeConversation, setActiveConversation] = useState(conversations[0]);
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [newMessage, setNewMessage] = useState('');

  useEffect(() => {
    if (activeConversation) {
      fetchMessages(activeConversation.id);
    }
  }, [activeConversation]);

  const fetchMessages = async (conversationId) => {
    try {
      setLoading(true);
      const response = await ApiService.getMessages(conversationId);
      if (response.success) {
        setMessages(response.data);
      } else {
        setError('获取消息失败');
      }
    } catch (err) {
      setError('获取消息时发生错误');
    } finally {
      setLoading(false);
    }
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (newMessage.trim() === '') return;

    const message = {
      id: messages.length + 1,
      sender: '我',
      content: newMessage,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };

    setMessages([...messages, message]);
    setNewMessage('');
    
    // 这里应该调用API发送消息
    // await ApiService.sendMessage(activeConversation.id, newMessage);
  };

  if (loading && messages.length === 0) {
    return <div className="messages-page">加载中...</div>;
  }

  if (error) {
    return <div className="messages-page error-message">{error}</div>;
  }

  return (
    <div className="messages-page">
      <div className="messages-container">
        <div className="conversations-panel">
          <div className="conversations-header">
            <h2>消息</h2>
          </div>
          <div className="conversations-list">
            {conversations.map(conversation => (
              <div 
                key={conversation.id} 
                className={`conversation-item ${activeConversation.id === conversation.id ? 'active' : ''}`}
                onClick={() => setActiveConversation(conversation)}
              >
                <img src={conversation.avatar} alt={conversation.username} className="conversation-avatar" />
                <div className="conversation-info">
                  <h3>{conversation.username}</h3>
                  <p>{conversation.lastMessage}</p>
                </div>
                <div className="conversation-meta">
                  <span className="timestamp">{conversation.timestamp}</span>
                  {conversation.unread > 0 && (
                    <span className="unread-count">{conversation.unread}</span>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="chat-panel">
          <div className="chat-header">
            <img src={activeConversation.avatar} alt={activeConversation.username} className="chat-avatar" />
            <h3>{activeConversation.username}</h3>
          </div>

          <div className="chat-messages">
            {messages.map(message => (
              <div 
                key={message.id} 
                className={`message ${message.sender === '我' ? 'sent' : 'received'}`}
              >
                <div className="message-content">
                  <p>{message.content}</p>
                  <span className="message-time">{message.timestamp}</span>
                </div>
              </div>
            ))}
          </div>

          <div className="message-input-container">
            <form onSubmit={handleSendMessage} className="message-form">
              <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                placeholder="输入消息..."
                className="message-input"
              />
              <button type="submit" className="send-button">发送</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Messages;