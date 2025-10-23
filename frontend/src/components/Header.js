import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './Header.css';
import { t, getCurrentLanguage, setLanguage, getSupportedLanguages } from '../services/i18n';
import { getCurrentTheme, getSupportedThemes, getThemeIcon, setTheme } from '../services/theme';

const Header = () => {
  const [currentLanguage, setCurrentLanguage] = useState(getCurrentLanguage());
  const [currentTheme, setCurrentTheme] = useState(getCurrentTheme());
  
  useEffect(() => {
    // 监听语言更改事件
    const handleLanguageChange = (event) => {
      setCurrentLanguage(event.detail);
    };
    
    // 监听主题更改事件
    const handleThemeChange = (event) => {
      setCurrentTheme(event.detail.theme);
    };
    
    window.addEventListener('languageChanged', handleLanguageChange);
    window.addEventListener('themeChanged', handleThemeChange);
    
    return () => {
      window.removeEventListener('languageChanged', handleLanguageChange);
      window.removeEventListener('themeChanged', handleThemeChange);
    };
  }, []);
  
  const handleLanguageChange = (event) => {
    const newLanguage = event.target.value;
    setLanguage(newLanguage);
  };
  
  const handleThemeChange = (event) => {
    const newTheme = event.target.value;
    setTheme(newTheme);
  };
  
  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="logo">
          Nexus
        </Link>
        <div className="header-spacer"></div>
        <nav className="nav">
          <select value={currentLanguage} onChange={handleLanguageChange} className="language-selector">
            {getSupportedLanguages().map(lang => (
              <option key={lang} value={lang}>
                {lang === 'zh' ? '中文' : 'English'}
              </option>
            ))}
          </select>
          <select value={currentTheme} onChange={handleThemeChange} className="theme-selector">
            {getSupportedThemes().map(theme => (
              <option key={theme} value={theme}>
                {getThemeIcon(theme)} {theme.charAt(0).toUpperCase() + theme.slice(1)}
              </option>
            ))}
          </select>
          <Link to="/login" className="nav-link">{t('auth.login')}</Link>
          <Link to="/register" className="nav-link">{t('auth.register')}</Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;