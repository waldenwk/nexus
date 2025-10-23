import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';
import { t } from '../services/i18n';

const Sidebar = () => {
  return (
    <div className="sidebar">
      <nav className="sidebar-nav">
        <ul>
          <li>
            <Link to="/" className="sidebar-link">
              <span className="icon">🏠</span>
              <span className="text">{t('nav.home')}</span>
            </Link>
          </li>
          <li>
            <Link to="/timeline" className="sidebar-link">
              <span className="icon">📰</span>
              <span className="text">{t('timeline.title')}</span>
            </Link>
          </li>
          <li>
            <Link to="/profile" className="sidebar-link">
              <span className="icon">👤</span>
              <span className="text">{t('nav.profile')}</span>
            </Link>
          </li>
          <li>
            <Link to="/friends" className="sidebar-link">
              <span className="icon">👥</span>
              <span className="text">{t('nav.friends')}</span>
            </Link>
          </li>
          <li>
            <Link to="/messages" className="sidebar-link">
              <span className="icon">💬</span>
              <span className="text">{t('nav.messages')}</span>
            </Link>
          </li>
          <li>
            <Link to="/album" className="sidebar-link">
              <span className="icon">🖼️</span>
              <span className="text">{t('nav.photos')}</span>
            </Link>
          </li>
          <li>
            <Link to="/settings" className="sidebar-link">
              <span className="icon">⚙️</span>
              <span className="text">{t('nav.settings')}</span>
            </Link>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;