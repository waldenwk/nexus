import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = () => {
  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="logo">
          Nexus
        </Link>
        <div className="header-spacer"></div>
        <nav className="nav">
          <Link to="/login" className="nav-link">登录</Link>
          <Link to="/register" className="nav-link">注册</Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;