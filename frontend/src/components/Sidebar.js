import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';

const Sidebar = () => {
  return (
    <div className="sidebar">
      <nav className="sidebar-nav">
        <ul>
          <li>
            <Link to="/" className="sidebar-link">
              <span className="icon">🏠</span>
              <span className="text">首页</span>
            </Link>
          </li>
          <li>
            <Link to="/timeline" className="sidebar-link">
              <span className="icon">📰</span>
              <span className="text">时间轴</span>
            </Link>
          </li>
          <li>
            <Link to="/profile" className="sidebar-link">
              <span className="icon">👤</span>
              <span className="text">个人资料</span>
            </Link>
          </li>
          <li>
            <Link to="/friends" className="sidebar-link">
              <span className="icon">👥</span>
              <span className="text">朋友</span>
            </Link>
          </li>
          <li>
            <Link to="/messages" className="sidebar-link">
              <span className="icon">💬</span>
              <span className="text">消息</span>
            </Link>
          </li>
          <li>
            <Link to="/album" className="sidebar-link">
              <span className="icon">🖼️</span>
              <span className="text">相册</span>
            </Link>
          </li>
          <li>
            <Link to="/settings" className="sidebar-link">
              <span className="icon">⚙️</span>
              <span className="text">设置</span>
            </Link>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;